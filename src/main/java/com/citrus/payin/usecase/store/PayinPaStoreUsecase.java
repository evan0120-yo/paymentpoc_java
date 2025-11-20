package com.citrus.payin.usecase.store;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import com.citrus.common.exception.DataErrorException;
import com.citrus.payin.event.PayinEvent;
import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.factory.channel.object.dto.PaPaymentDto;
import com.citrus.payin.object.bo.PayinBo;
import com.citrus.payin.object.dto.CallbackDto;
import com.citrus.payin.object.dto.ExecutePaDto;
import com.citrus.payin.object.dto.SyncOrderDto;
import com.citrus.payin.object.event.FireCallbackFailedEvent;
import com.citrus.payin.object.event.FireCallbackSucceededEvent;
import com.citrus.payin.object.event.FirePaValidatedEvent;
import com.citrus.payin.object.req.InitiatePaymentReq;
import com.citrus.payin.service.query.PayinQueryService;
import com.citrus.payin.service.store.PayinStoreService;
import com.fasterxml.uuid.Generators;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinPaStoreUsecase {

	private final PayinPaRouteUsecase payinPaRouteUsecase;
	private final PayinEvent payinEvent;
	private final TransactionTemplate transactionTemplate;
	private final PayinStoreService payinStoreService;
	private final PayinQueryService payinQueryService;

	public PaPaymentDto initiatePaPayment(InitiatePaymentReq req) {
		String orderGid = Generators.timeBasedEpochGenerator().generate().toString();
		// String orderGid = "123";
		String rechargeGid = Generators.timeBasedEpochGenerator().generate().toString();
		String userGid = req.getUserGid();
		String refId = req.getRefId();
		String rechargeInfo = req.getRechargeInfo();
		BigDecimal actualPaymentAmount = req.getActualPaymentAmount();
		PaPaymentDto paPaymentDto = new PaPaymentDto();
		// 1. user是否存在(這裡跳過)
		// 2. 檢查金額是否正確並且檢查billid是否存在
		// 3. init paycore order單
		FirePaValidatedEvent event = FirePaValidatedEvent.builder()
				.userGid(userGid)
				.orderGid(orderGid)
				.rechargeGid(rechargeGid)
				.refId(refId)
				.actualPaymentAmount(actualPaymentAmount)
				// 先假資料因為這要看實際Biller金額
				.billAmount(new BigDecimal("199.00"))
				.rechargeInfo(rechargeInfo)
				.build();
		payinEvent.firePaValidated(event);
		// 3. choice channel -> call甲方 and save payin
		// 這裡因為需求目前狀況不需要原子性
		ExecutePaDto executePaDto = ExecutePaDto.builder()
				.orderGid(orderGid)
				.rechargeGid(rechargeGid)
				.userGid(userGid)
				.billGid(req.getBillGid())
				.refId(refId)
				.actualPaymentAmount(actualPaymentAmount)
				.rechargeInfo(rechargeInfo)
				.build();
		payinPaRouteUsecase.executePa(executePaDto);
		return paPaymentDto;
	}

	public void handlePaCallback(CallbackDto dto) {
		// 1. apapter to handle callback
		final CallbackPaDto callbackPaDto = payinPaRouteUsecase.handleCallbackData(dto);
		PayinBo payinBo = transactionTemplate.execute(status -> {
			// 2. findpayin record and attempt
			PayinBo bo = payinQueryService.queryPayinBo(callbackPaDto.getRefId());
			if (!bo.hasOrder()) {
				// 3-1. save retry callback
				FireCallbackFailedEvent event = FireCallbackFailedEvent.builder()
						.refId(callbackPaDto.getRefId())
						.channel(callbackPaDto.getChannel())
						.rawBody(callbackPaDto.getRawBody())
						.headers(callbackPaDto.getHeaders())
						.build();
				payinEvent.fireCallbackFailed(event);
				// 3-2. return
				return bo;
			}
			// 4. update payin record and attempt
			bo = payinStoreService.updatePaCallbackPreCheckBo(bo);
			// 5. insert log
			return payinStoreService.savePaCallbackLog(bo, callbackPaDto);
		});
		// 7. adapter -> handle check callback
		SyncOrderDto syncOrderDto = SyncOrderDto.builder()
				.refId(payinBo.getPayinRecord().getRefId())
				.callbackChannel(dto.getCallbackChannel())
				.payinBo(payinBo)
				.build();
		final CallbackPaDto callbackPaDtoSync = payinPaRouteUsecase.syncOrderStatus(syncOrderDto);

		transactionTemplate.execute(status -> {
			// 8. find and guard payin bo
			PayinBo bo = payinQueryService.querySyncOrder(syncOrderDto);
			// 9. update payin record
			bo = payinStoreService.updatePaCallbackBo(bo, callbackPaDtoSync);
			// 10. publish > save outbox
			FireCallbackSucceededEvent event = FireCallbackSucceededEvent.builder()
					.refId(syncOrderDto.getRefId())
					.build();
			payinEvent.fireCallbackSucceeded(event);
			return null;
		});
	}

}
