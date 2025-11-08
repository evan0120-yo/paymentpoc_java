package com.citrus.payin.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payin.enums.PayinAttemptStatusEnum;
import com.citrus.payin.model.PayinAttempt;
import com.citrus.payin.repository.PayinAttemptRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayinAttemptDao {
	
	private final PayinAttemptRepository payinAttemptRepository;
	
	public PayinAttempt save(PayinAttempt payinAttempt) {
		Timestamp now = Timestamp.now();
		payinAttempt.setPayinAttemptId(Generators.timeBasedEpochGenerator().generate().toString());
		payinAttempt.setCreatedTime(now);
		payinAttempt.setUpdateTime(now);
		return payinAttemptRepository.save(payinAttempt);
	}
	
	public PayinAttempt update(PayinAttempt payinAttempt) {
		Timestamp now = Timestamp.now();
		payinAttempt.setUpdateTime(now);
		return payinAttemptRepository.save(payinAttempt);
	}
	
	public List<PayinAttempt> findByRefIdWithPaSuccess(String payinAttemptId) {
		return payinAttemptRepository.findByRefIdAndPayinAttemptStatus(payinAttemptId, PayinAttemptStatusEnum .INITIATE_PAYMENT_SUCCESS);
	}
	
	public PayinAttempt findById(String payinAttemptId) {
		return payinAttemptRepository.findById(payinAttemptId).orElseThrow(() -> new DataNotFoundException());
	}
}
