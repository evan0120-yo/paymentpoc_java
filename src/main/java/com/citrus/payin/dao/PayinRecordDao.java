package com.citrus.payin.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payin.enums.PayinStatusEnum;
import com.citrus.payin.model.PayinRecord;
import com.citrus.payin.repository.PayinRecordRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PayinRecordDao {

	private final PayinRecordRepository payinRecordRepository;

	public PayinRecord save(PayinRecord payinRecord) {
		Timestamp now = Timestamp.now();
		payinRecord.setPayinRecordId(Generators.timeBasedEpochGenerator().generate().toString());
		payinRecord.setPayinStatus(PayinStatusEnum.INIT);
		payinRecord.setCreatedTime(now);
		payinRecord.setUpdateTime(now);
		return payinRecordRepository.save(payinRecord);
	}

	public PayinRecord update(PayinRecord payinRecord) {
		Timestamp now = Timestamp.now();
		payinRecord.setUpdateTime(now);
		return payinRecordRepository.save(payinRecord);
	}

	public List<PayinRecord> findByRefId(String refId) {
		return payinRecordRepository.findByRefId(refId);
	}

	public PayinRecord findById(String payinRecordId) {
		return payinRecordRepository.findById(payinRecordId).orElseThrow(() -> new DataNotFoundException());
	}
}
