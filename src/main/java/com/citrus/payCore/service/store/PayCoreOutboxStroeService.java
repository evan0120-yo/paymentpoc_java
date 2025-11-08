package com.citrus.payCore.service.store;

import org.springframework.stereotype.Service;

import com.citrus.payCore.dao.PayCoreOutboxDao;
import com.citrus.payCore.enums.PayCoreEventEnum;
import com.citrus.payCore.model.PayCoreOutbox;
import com.citrus.payCore.object.event.FireRechargeSuccessEvent;
import com.citrus.payin.enums.PayinEventEnum;
import com.fasterxml.uuid.Generators;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreOutboxStroeService {
	
	private final PayCoreOutboxDao payCoreOutboxDao;

	public PayCoreOutbox saveRechargeSuccess(FireRechargeSuccessEvent event, PayCoreEventEnum eventType) {
		PayCoreOutbox payCoreOutbox = PayCoreOutbox.builder()
				.payCoreOutboxId(Generators.timeBasedEpochGenerator().generate().toString())
				.refId(event.getRefId())
				.actualPaymentAmount(event.getActualPaymentAmount())
				.billAmount(event.getBillAmount())
				.rechargeInfo(event.getRechargeInfo())
				.eventType(eventType.name())
				.topicId(event.getTopicId())
				.build();
		return payCoreOutboxDao.save(payCoreOutbox);
	}
	
	public PayCoreOutbox save(String aggregateId, PayinEventEnum eventType) {
		PayCoreOutbox payCoreOutbox = PayCoreOutbox.builder()
				.payCoreOutboxId(Generators.timeBasedEpochGenerator().generate().toString())
				.aggregateId(aggregateId)
				.eventType(eventType.name())
				.build();
		return payCoreOutboxDao.save(payCoreOutbox);
	}
	
	public void claimPendingTasks(String instanceId) {
		payCoreOutboxDao.claimPendingTasks(instanceId);
	}
	
	public void updateStatusByInstanceId(String instanceId) {
		payCoreOutboxDao.updateStatusByInstanceId(instanceId);
	}
}
