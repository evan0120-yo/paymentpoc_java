package com.citrus.payin.event;

import org.springframework.stereotype.Component;

import com.citrus.payin.enums.PayinEventEnum;
import com.citrus.payin.object.event.FireCallbackFailedEvent;
import com.citrus.payin.object.event.FireCallbackSucceededEvent;
import com.citrus.payin.object.event.FirePaValidatedEvent;
import com.citrus.payin.usecase.store.PayinOutboxStoreUsecasae;
import com.citrus.share.enums.TopicEnum;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayinEvent {
	
	private final PayinOutboxStoreUsecasae payinOutboxStoreUsecasae;

	public void firePaValidated(FirePaValidatedEvent event) {
		System.out.println("firePaValidated, :"+event);
		// 1. publish to paycore
		event.setEventType(PayinEventEnum.RECHARGE_INIT.name());
		event.setTopicId(TopicEnum.INIT_EVENT.getTopicId());
		payinOutboxStoreUsecasae.firePaValidated(event);
	}
	
	public void fireCallbackSucceeded(FireCallbackSucceededEvent event) {
		System.out.println("fireCallbackSucceeded, :"+event);
		event.setEventType(PayinEventEnum.CALLBACK_SUCCESSED.name());
		event.setTopicId(TopicEnum.CALLBACK_SUCCESS.getTopicId());
		payinOutboxStoreUsecasae.saveCallbackSuccess(event);
	}
	
	public void fireCallbackFailed(FireCallbackFailedEvent event) {
		System.out.println("fireCallbackFailed, :"+event);
		// 代表連payin都沒有的資料，也許會記錄warn log保存，或是看需求要不要送到payLedger
	}
}
