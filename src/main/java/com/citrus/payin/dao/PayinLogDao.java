package com.citrus.payin.dao;

import org.springframework.stereotype.Component;

import com.citrus.payin.model.PayinLog;
import com.citrus.payin.repository.PayinLogRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayinLogDao {

	private final PayinLogRepository payinLogRepository;

	public PayinLog save(PayinLog payinLog) {
		Timestamp now = Timestamp.now();
		payinLog.setCreateTime(now);
		payinLog.setPayLogId(Generators.timeBasedEpochGenerator().generate().toString());
		return payinLogRepository.save(payinLog);
	}
}
