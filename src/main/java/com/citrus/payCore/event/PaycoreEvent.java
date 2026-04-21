package com.citrus.payCore.event;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.citrus.payCore.enums.PayCoreEventEnum;
import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.model.BbpsRecharge;
import com.citrus.payCore.model.RechargeRetry;
import com.citrus.payCore.object.bo.PayCoreBo;
import com.citrus.payCore.object.event.FireOrderNotFoundEvent;
import com.citrus.payCore.object.event.FireRechargeSuccessEvent;
import com.citrus.payCore.object.req.RechargeRetrySaveReq;
import com.citrus.payCore.usecase.query.RechargeRetryQueryUsecase;
import com.citrus.payCore.usecase.store.PayCoreOutboxStoreUsecase;
import com.citrus.payCore.usecase.store.RechargeRetryStoreUsecase;
import com.citrus.share.enums.TopicEnum;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PaycoreEvent {
	
	private final PayCoreOutboxStoreUsecase payCoreOutboxStoreUsecase;
	private final RechargeRetryStoreUsecase rechargeRetryStoreUsecase;
	private final RechargeRetryQueryUsecase rechargeRetryQueryUsecase;

	public void firePaymentOrderInit(PayCoreBo payCoreBo) {
		System.out.println("createPaymentOrder, payCoreBo:"+payCoreBo);
	}
	
	public void fireRechargeSuccess(PayCoreBo payCoreBo) {
		System.out.println("fireRechargeSuccess, payCoreBo:"+payCoreBo);
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
		FireRechargeSuccessEvent event = FireRechargeSuccessEvent.builder()
				.orderGid(bbpsOrder.getOrderGid())
				.refId(bbpsOrder.getRefId())
				.actualPaymentAmount(bbpsOrder.getActualPaymentAmount())
				.billAmount(bbpsOrder.getBillAmount())
				.rechargeInfo(bbpsRecharge.getRechargeInfo())
				.build();
		event.setEventType(PayCoreEventEnum.RECHARGE_SUCCEEDED.name());
		event.setTopicId(TopicEnum.RECHARGE_SUCCESS.getTopicId());
		payCoreOutboxStoreUsecase.saveRechargeSuccess(event);
	}
	
	public void fireOrderNotFound(FireOrderNotFoundEvent event) {
		System.out.println("fireOrderNotFound, event:"+event);
//		event.setEventType(PayCoreEventEnum.RECHARGE_SUCCEEDED.name());
//		event.setTopicId(TopicEnum.RECHARGE_SUCCESS.getTopicId());
		// 1. findByRefId
		Optional<RechargeRetry> rechargeRetryOpt = rechargeRetryQueryUsecase.findByRefId(event.getRefId());
		// 2. save in database for retry
		if(rechargeRetryOpt.isEmpty()) {
			RechargeRetrySaveReq req = RechargeRetrySaveReq.builder()
					.refId(event.getRefId())
					.build();
			rechargeRetryStoreUsecase.save(req);
		} else {
			System.out.println("rechargeRetryOpt is present, rechargeRetry:"+rechargeRetryOpt.get());
		}
		
	}
	
//	public void fireRechargeFailure(PayCoreBo payCoreBo) {
//		System.out.println("handleRechargeFailure, payCoreBo:"+payCoreBo);
//		bbpsOutboxStoreUsecase.saveRechargeFail(payCoreBo.getBbpsOrder().getOrderGid());
//	}
	
//	public void processBillPayment(PayCoreBo payCoreBo) {
//		System.out.println("processBillPayment, payCoreBo:"+payCoreBo);
//	}
//	
//	public void handlePaymentSuccess(PayCoreBo payCoreBo) {
//		System.out.println("handlePaymentSuccess, payCoreBo:"+payCoreBo);
//		bbpsOutboxStoreUsecase.savePaymentSuccess(payCoreBo.getBbpsOrder().getOrderGid());
//	}
//	
//	public void handlePaymentFailure(PayCoreBo payCoreBo) {
//		System.out.println("handlePaymentFailure, payCoreBo:"+payCoreBo);
//		bbpsOutboxStoreUsecase.savePaymentFail(payCoreBo.getBbpsOrder().getOrderGid());
//	}
}
