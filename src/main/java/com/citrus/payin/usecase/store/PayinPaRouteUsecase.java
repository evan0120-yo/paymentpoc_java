package com.citrus.payin.usecase.store;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.citrus.common.exception.DataErrorException;
import com.citrus.payin.factory.callback.PayinCallbackFactory;
import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.factory.channel.PayinChannelFactory;
import com.citrus.payin.factory.channel.object.bo.PaPaymentBo;
import com.citrus.payin.factory.channel.object.bo.PaPaymentBo.BilldeskPaPaymentBo;
import com.citrus.payin.factory.channel.object.bo.PaPaymentBo.MpursePaPaymentBo;
import com.citrus.payin.factory.channel.object.dto.PaPaymentDto;
import com.citrus.payin.factory.channel.object.dto.SavePaPayinDto;
import com.citrus.payin.factory.strategy.PayinStrategyEnum;
import com.citrus.payin.factory.strategy.PayinStrategyFactory;
import com.citrus.payin.model.PayinRecord;
import com.citrus.payin.object.dto.CallbackDto;
import com.citrus.payin.object.dto.ExecutePaDto;
import com.citrus.payin.object.dto.SyncOrderDto;
import com.citrus.payin.object.event.FirePaValidatedEvent;
import com.citrus.payin.object.req.InitiatePaymentReq;
import com.citrus.payin.service.store.PayinRouteStoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinPaRouteUsecase {
	
	private final Map<String, PayinChannelFactory> payinChannelMap;
	private final Map<String, PayinStrategyFactory> payinStrategyMap;
	private final Map<String, PayinCallbackFactory> payinCallbackMap;
	private final PayinRouteStoreService payinRouteStoreService;
	
	public PaPaymentDto executePa(ExecutePaDto executePaDto) {
		List<String> payinChannelKeyList = choicePaChannel(executePaDto);
		if(payinChannelKeyList.isEmpty()) {
			throw new DataErrorException("沒有channel支援這個bill");
		}
		PayinRecord payinRecord = null;
		for(String key : payinChannelKeyList) {
			PaPaymentDto paPaymentDto = new PaPaymentDto();
			PaPaymentBo bo = PaPaymentBo.builder()
					.mpursePaPaymentBo(MpursePaPaymentBo.builder().amount(executePaDto.getActualPaymentAmount()).build())
					.billdeskPaPaymentBo(BilldeskPaPaymentBo.builder().build())
					.build();
			paPaymentDto = payinChannelMap.get(key).initiatePaPayment(bo);
			System.out.println("out put dto:"+paPaymentDto);
			// save payin record等等
			SavePaPayinDto savePaPayinDto = SavePaPayinDto.builder()
					.orderGid(executePaDto.getOrderGid())
					.rechargeGid(executePaDto.getRechargeGid())
					.userGid(executePaDto.getUserGid())
					.billGid(executePaDto.getBillGid())
					.refId(executePaDto.getRefId())
					.actualPaymentAmount(executePaDto.getActualPaymentAmount())
					.rechargeInfo(executePaDto.getRechargeInfo())
					.paPaymentDto(paPaymentDto)
					.payinRecord(payinRecord)
					.build();
			payinRecord = payinRouteStoreService.savePaPayinBo(savePaPayinDto);
			if(paPaymentDto.getIsSuccess()) {
				return paPaymentDto;
			}
		}
		throw new DataErrorException("所有channel都失敗了");
	}
	
	public CallbackPaDto handleCallbackData(CallbackDto dto) {
		// 1. adapter -> handle req (trans)
		return payinCallbackMap.get(dto.getCallbackChannel().getClassName()).handleCallbackData(dto);
	}
	
	public CallbackPaDto syncOrderStatus(SyncOrderDto dto) {
		// 1. adapter -> handle check callback
		return payinCallbackMap.get(dto.getCallbackChannel().getClassName()).syncOrderStatus(dto);
	}
	
	private List<String> choicePaChannel(ExecutePaDto executePaDto) {
		// 這裡寫排序手法，來確定哪優先執行哪個，示範的部分先單純都有Mpurse
		String choiceStrategyStr = PayinStrategyEnum.DEFAULT.getClassName();
		// 下面使用工廠來做  也可以單純if else 也可以串接apollo讓pm手動去改
		// 不太建議存RDBMS這樣感覺會太慢
		return payinStrategyMap.get(choiceStrategyStr).defaultStrategy(executePaDto);
	}
}
