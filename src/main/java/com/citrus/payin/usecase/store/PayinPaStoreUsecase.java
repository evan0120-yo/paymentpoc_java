package com.citrus.payin.usecase.store;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.citrus.payin.event.PayinEvent;
import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.factory.channel.object.dto.PaPaymentDto;
import com.citrus.payin.object.bo.PayinBo;
import com.citrus.payin.object.dto.CallbackDto;
import com.citrus.payin.object.dto.ExecutePaDto;
import com.citrus.payin.object.dto.SyncOrderDto;
import com.citrus.payin.object.event.FirePaValidatedEvent;
import com.citrus.payin.object.req.InitiatePaymentReq;
import com.fasterxml.uuid.Generators;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinPaStoreUsecase {
	
    private final PayinPaRouteUsecase payinPaRouteUsecase;
    private final CallbackPaUsecase callbackPaUsecase;
    private final PayinEvent payinEvent;

	public PaPaymentDto initiatePaPayment(InitiatePaymentReq req) {
		String orderGid = Generators.timeBasedEpochGenerator().generate().toString();
//		String orderGid = "123";
		String rechargeGid = Generators.timeBasedEpochGenerator().generate().toString();
		String userGid = req.getUserGid();
		String refId = req.getRefId();
		String rechargeInfo = req.getRechargeInfo();
		BigDecimal actualPaymentAmount = req.getActualPaymentAmount();
		// 1. user是否存在(這裡跳過)
		// 2. 檢查金額是否正確並且檢查billid是否存在
		// 3. init paycore order單
		FirePaValidatedEvent event = FirePaValidatedEvent.builder()
				.userGid(userGid)
				.orderGid(orderGid)
				.rechargeGid(rechargeGid)
				.refId(refId)
				.actualPaymentAmount(actualPaymentAmount)
				.billAmount(new BigDecimal("199.00"))
				.rechargeInfo(rechargeInfo)
				.build();
		payinEvent.firePaValidated(event);
		// 3. choice channel -> call甲方 and save payin
		ExecutePaDto executePaDto = ExecutePaDto.builder()
				.orderGid(orderGid)
				.rechargeGid(rechargeGid)
				.userGid(userGid)
				.billGid(req.getBillGid())
				.refId(refId)
				.actualPaymentAmount(actualPaymentAmount)
				.rechargeInfo(rechargeInfo)
				.build();
		PaPaymentDto paPaymentDto = payinPaRouteUsecase.executePa(executePaDto);
		// 4. make return resp
		return paPaymentDto;
	}
	
	public void handlePaCallback(CallbackDto dto) {
		// 1. apapter to handle callback
		CallbackPaDto preDto = payinPaRouteUsecase.handleCallbackData(dto);
		// 2. update payin
		PayinBo payinBo = callbackPaUsecase.updatePaCallbackPreCheck(preDto);
		if(!payinBo.hasOrder()) {
			return;
		}
		// 3. adapter -> handle check callback
		SyncOrderDto syncOrderDto = SyncOrderDto.builder()
				.refId(payinBo.getPayinRecord().getRefId())
				.callbackChannel(dto.getCallbackChannel())
				.payinBo(payinBo)
				.build();
		CallbackPaDto callbackPaDto = payinPaRouteUsecase.syncOrderStatus(syncOrderDto);
		// 4. adapter to check recharge and update payin
		callbackPaUsecase.syncOrderStatus(syncOrderDto, callbackPaDto);
	}
}
