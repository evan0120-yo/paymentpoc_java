package com.citrus.payin.service.store;

import org.springframework.stereotype.Service;

import com.citrus.payin.dao.PayinOutboxDao;
import com.citrus.payin.enums.PayinEventEnum;
import com.citrus.payin.model.PayinOutbox;
import com.citrus.payin.object.event.FireCallbackSucceededEvent;
import com.citrus.payin.object.event.FirePaValidatedEvent;
import com.fasterxml.uuid.Generators;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinOutboxStoreService {

	private final PayinOutboxDao payinOutboxDao;

	public PayinOutbox firePaValidated(FirePaValidatedEvent event, PayinEventEnum eventType) {
		PayinOutbox payinOutbox = PayinOutbox.builder()
				.payinOutboxId(Generators.timeBasedEpochGenerator().generate().toString())
				.userGid(event.getUserGid())
				.orderGid(event.getOrderGid())
				.rechargeGid(event.getRechargeGid())
				.refId(event.getRefId())
				.actualPaymentAmount(event.getActualPaymentAmount())
				.billAmount(event.getBillAmount())
				.rechargeInfo(event.getRechargeInfo())
				.eventType(eventType.name())
				.topicId(event.getTopicId())
				.build();
		return payinOutboxDao.save(payinOutbox);
	}
	
	public PayinOutbox saveCallbackSuccess(FireCallbackSucceededEvent event, PayinEventEnum eventType) {
		PayinOutbox payinOutbox = PayinOutbox.builder()
				.payinOutboxId(Generators.timeBasedEpochGenerator().generate().toString())
				.refId(event.getRefId())
				.eventType(eventType.name())
				.topicId(event.getTopicId())
				.build();
		return payinOutboxDao.save(payinOutbox);
	}
	
	public PayinOutbox save(String aggregateId, PayinEventEnum eventType) {
		PayinOutbox payinOutbox = PayinOutbox.builder()
				.payinOutboxId(Generators.timeBasedEpochGenerator().generate().toString())
				.aggregateId(aggregateId)
				.eventType(eventType.name())
				.build();
		return payinOutboxDao.save(payinOutbox);
	}
	
	public void claimPendingTasks(String instanceId) {
		payinOutboxDao.claimPendingTasks(instanceId);
	}
	
	public void updateStatusByInstanceId(String instanceId) {
		payinOutboxDao.updateStatusByInstanceId(instanceId);
	}
}
