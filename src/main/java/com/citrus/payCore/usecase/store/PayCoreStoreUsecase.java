package com.citrus.payCore.usecase.store;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.payCore.event.PaycoreEvent;
import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.object.bo.PayCoreBo;
import com.citrus.payCore.object.dto.PayCoreInitDto;
import com.citrus.payCore.object.dto.PayCoreRechargeSuccessDto;
import com.citrus.payCore.object.event.FireOrderNotFoundEvent;
import com.citrus.payCore.service.guard.PayCoreGuardService;
import com.citrus.payCore.service.query.PayCoreQueryService;
import com.citrus.payCore.service.store.PayCoreStoreService;
import com.citrus.payCore.usecase.query.PayCoreQueryUsecase;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreStoreUsecase {
	
	private final PayCoreQueryUsecase payCoreQueryUsecase;
	private final PayCoreStoreService payCoreStoreService;
	private final PayCoreQueryService payCoreQueryService;
	private final PayCoreGuardService payCoreGuardService;
	private final PaycoreEvent paycoreEvent;
	

	@Transactional
	public PayCoreBo createPaymentOrderInit(PayCoreInitDto dto) {
		PayCoreBo updated = new PayCoreBo();
		// 1. findByRefid
		List<BbpsOrder> bbpsOrderList = payCoreQueryService.findOrderByRefId(dto.getRefId());
		// TODO 這裡先粗暴擋掉但是實際上要看PM  有眾多發生狀況 例如A使用者創立單張不繳費 使用者B想幫使用者A繳費的話可以嗎等等
		if(bbpsOrderList.isEmpty()) {
			// 2. create
			updated = payCoreStoreService.createPaymentOrderInit(dto.getOrderGid(), dto.getRechargeGid(), dto.getUserGid(), dto.getRefId(), dto.getActualPaymentAmount(), dto.getBillAmount(), dto.getRechargeInfo());
			// 3. paycore Event
			paycoreEvent.firePaymentOrderInit(updated);
		} else {
			// 重複新增發送的event
			
		}
		return updated;
	}
	
	@Transactional("jpaTxManager")
	public PayCoreBo handleRechargeSuccess(PayCoreRechargeSuccessDto dto) {
		// 1. query paycoreBo
		PayCoreBo payCoreBo = payCoreQueryUsecase.queryPayCoreBo(dto.getRefId());
		if(payCoreBo.getBbpsOrder() == null) {
			// TODO 進入Retry循環
			FireOrderNotFoundEvent event = FireOrderNotFoundEvent.builder()
					.refId(dto.getRefId())
					.build();
			paycoreEvent.fireOrderNotFound(event);
			return payCoreBo;
		}
		// 2. guardpayCoreBo
		payCoreGuardService.handleRechargeSuccessGuard(payCoreBo);
		// 3. update status
		PayCoreBo updated = payCoreStoreService.handleRechargeSuccess(payCoreBo);
		// 4. paycore Event
		paycoreEvent.fireRechargeSuccess(updated);
		return updated;
	}
	
//	@Transactional("jpaTxManager")
//	public PayCoreBo handleRechargeFailure(PayCoreBo payCoreBo) {
//		// 1. guard
//		payCoreGuardService.handleRechargeFailureGuard(payCoreBo);
//		// 2. update status
//		PayCoreBo updated = payCoreStoreService.handleRechargeFailure(payCoreBo);
//		// 3. paycore Event
//		paycoreEvent.fireRechargeFailure(updated);
//		return updated;
//	}
	
//	@Transactional("jpaTxManager")
//	public PayCoreBo handlePaymentSuccess(PayCoreBo payCoreBo) {
//		// 1. guard
//		payCoreGuardService.handlePaymentSuccessGuard(payCoreBo);
//		// 2. update status
//		PayCoreBo updated = payCoreStoreService.handlePaymentSuccess(payCoreBo);
//		// 3. paycore Event
//		paycoreEvent.firePaymentSuccess(updated);
//		return updated;
//	}
//	
//	@Transactional("jpaTxManager")
//	public PayCoreBo handlePaymentFailure(PayCoreBo payCoreBo) {
//		// 1. guard
//		payCoreGuardService.handlePaymentFailureGuard(payCoreBo);
//		// 2. update status
//		PayCoreBo updated = payCoreStoreService.handlePaymentFailure(payCoreBo);
//		// 3. paycore Event
//		paycoreEvent.handlePaymentFailure(updated);
//		return updated;
//	}
}
