package com.citrus.payCore.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.payCore.enums.PayCoreOutboxTypeEnum;
import com.citrus.payCore.model.PayCoreOutbox;
import com.citrus.payCore.repository.PayCoreOutboxRepository;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayCoreOutboxDao {

private final PayCoreOutboxRepository payCoreOutboxRepository;
	
	public PayCoreOutbox save(PayCoreOutbox payCoreOutbox) {
		Timestamp now = Timestamp.now();
		payCoreOutbox.setCreatedTime(now);
		payCoreOutbox.setAggregateType(PayCoreOutboxTypeEnum.PAYCORE.name());
		payCoreOutbox.setStatus(OutboxStatusEnum.PENDING);
		return payCoreOutboxRepository.save(payCoreOutbox);
	}
	
	public void claimPendingTasks(String instanceId) {
		payCoreOutboxRepository.claimPendingTasks(instanceId, 100);
	}
	
	public void updateStatusByInstanceId(String instanceId) {
		payCoreOutboxRepository.updateStatusByInstanceId(OutboxStatusEnum.SUCCESS, instanceId);
	}
	
	public List<PayCoreOutbox> findInIds(List<String> payinOutboxId){
		return payCoreOutboxRepository.findAllById(payinOutboxId);
	}
	
	public List<PayCoreOutbox> findByInstanceId(String instanceId, OutboxStatusEnum outboxStatus){
		return payCoreOutboxRepository.findByInstanceIdAndStatus(instanceId, outboxStatus);
	}
}
