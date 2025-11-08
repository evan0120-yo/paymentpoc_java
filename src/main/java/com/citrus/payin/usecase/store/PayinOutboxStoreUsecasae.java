package com.citrus.payin.usecase.store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.payin.enums.PayinEventEnum;
import com.citrus.payin.model.PayinOutbox;
import com.citrus.payin.object.event.FireCallbackSucceededEvent;
import com.citrus.payin.object.event.FirePaValidatedEvent;
import com.citrus.payin.service.store.PayinOutboxStoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinOutboxStoreUsecasae {


	private final PayinOutboxStoreService payinOutboxStoreService;
	
	public PayinOutbox firePaValidated(FirePaValidatedEvent event) {
		return payinOutboxStoreService.firePaValidated(event, PayinEventEnum.RECHARGE_INIT);
	}

	public PayinOutbox saveCallbackSuccess(FireCallbackSucceededEvent event) {
		return payinOutboxStoreService.saveCallbackSuccess(event, PayinEventEnum.CALLBACK_SUCCESSED);
	}
	
	public PayinOutbox saveCallbackSuccessFail(String aggregateId) {
		return payinOutboxStoreService.save(aggregateId, PayinEventEnum.CALLBACK_FAILED);
	}
	
	@Transactional
	public void claimPendingTasks(String instanceId) {
		// 1. update outbox
		payinOutboxStoreService.claimPendingTasks(instanceId);
	}
	
	@Transactional
	public void updateStatusByInstanceId(String instanceId) {
		// 1. update outbox
		payinOutboxStoreService.updateStatusByInstanceId(instanceId);
	}
}
