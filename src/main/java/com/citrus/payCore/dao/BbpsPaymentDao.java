package com.citrus.payCore.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payCore.enums.BbpsPaymentStatusEnum;
import com.citrus.payCore.model.BbpsPayment;
import com.citrus.payCore.object.bo.BbpsPaymentQueryBo;
import com.citrus.payCore.repository.BbpsPaymentRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BbpsPaymentDao {

	private final BbpsPaymentRepository bbpsPaymentRepository;
	
	public BbpsPayment saveInit(BbpsPayment bbpsPayment) {
		Timestamp now = Timestamp.now();
		bbpsPayment.setPaymentGid(Generators.timeBasedEpochGenerator().generate().toString());
		bbpsPayment.setCreateTime(now);
		bbpsPayment.setUpdateTime(now);
		bbpsPayment.setBbpsPaymentStatus(BbpsPaymentStatusEnum.INIT);
		return bbpsPaymentRepository.save(bbpsPayment);
	}
	
	public BbpsPayment updateStatus(BbpsPayment bbpsPayment) {
		Timestamp now = Timestamp.now();
		bbpsPayment.setUpdateTime(now);
		return bbpsPaymentRepository.save(bbpsPayment);
	}
	
	public BbpsPayment findById(String paymentGid) {
		return bbpsPaymentRepository.findById(paymentGid).orElseThrow(() -> new DataNotFoundException());
	}
	
	public List<BbpsPayment> queryBbpsPayment(BbpsPaymentQueryBo bo) {
		return bbpsPaymentRepository.queryBbpsPayment(bo.getOrderGid(), bo.getPaymentGid());
	}
	
	public BbpsPayment findByOrderGid(String orderGid) {
		List<BbpsPayment> bbpsPaymentList = bbpsPaymentRepository.findByOrderGid(orderGid);
		if(bbpsPaymentList.isEmpty()) {
			throw new DataNotFoundException();
		}
		return bbpsPaymentList.get(0);
	}
}
