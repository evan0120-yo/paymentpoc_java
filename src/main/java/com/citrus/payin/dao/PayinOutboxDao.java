package com.citrus.payin.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.payin.enums.PayinOutboxTypeEnum;
import com.citrus.payin.model.PayinOutbox;
import com.citrus.payin.repository.PayinOutboxRepository;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayinOutboxDao {
	
	private final PayinOutboxRepository payinOutboxRepository;
	
	public PayinOutbox save(PayinOutbox payinOutbox) {
		Timestamp now = Timestamp.now();
		payinOutbox.setCreatedTime(now);
		payinOutbox.setAggregateType(PayinOutboxTypeEnum.PAYIN.name());
		payinOutbox.setStatus(OutboxStatusEnum.PENDING);
		return payinOutboxRepository.save(payinOutbox);
	}
	
	public void claimPendingTasks(String instanceId) {
		payinOutboxRepository.claimPendingTasks(instanceId, 100);
	}
	
	public void updateStatusByInstanceId(String instanceId) {
		payinOutboxRepository.updateStatusByInstanceId(OutboxStatusEnum.SUCCESS, instanceId);
	}
	
	public List<PayinOutbox> findInIds(List<String> payinOutboxId){
		return payinOutboxRepository.findAllById(payinOutboxId);
	}
	
	public List<PayinOutbox> findByInstanceId(String instanceId, OutboxStatusEnum outboxStatus){
		return payinOutboxRepository.findByInstanceIdAndStatus(instanceId, outboxStatus);
	}
}
