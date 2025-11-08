package com.citrus.payCore.dao;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payCore.enums.BbpsRefundStatusEnum;
import com.citrus.payCore.model.BbpsRefund;
import com.citrus.payCore.repository.BbpsRefundRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BbpsRefundDao {

	private final BbpsRefundRepository bbpsRefundRepository;
	
	public BbpsRefund save(BbpsRefund bbpsRefund) {
		Timestamp now = Timestamp.now();
		bbpsRefund.setRefundGid(Generators.timeBasedEpochGenerator().generate().toString());
		bbpsRefund.setCreateTime(now);
		bbpsRefund.setUpdateTime(now);
		bbpsRefund.setBbpsRefundStatus(BbpsRefundStatusEnum.INIT);
		return bbpsRefundRepository.save(bbpsRefund);
	}
	
	public BbpsRefund updateStatus(BbpsRefund bbpsRefund, BbpsRefundStatusEnum enums) {
		Timestamp now = Timestamp.now();
		bbpsRefund.setCreateTime(now);
		bbpsRefund.setUpdateTime(now);
		bbpsRefund.setBbpsRefundStatus(enums);
		return bbpsRefundRepository.save(bbpsRefund);
	}
	
	public BbpsRefund findById(String refundGid) {
		return bbpsRefundRepository.findById(refundGid).orElseThrow(() -> new DataNotFoundException());
	}
}
