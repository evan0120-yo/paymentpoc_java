package com.citrus.payCore.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payCore.enums.BbpsOrderStatusEnum;
import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.object.bo.BbpsOrderQueryBo;
import com.citrus.payCore.repository.BbpsOrderRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BbpsOrderDao {

	private final BbpsOrderRepository bbpsOrderRepository;
	
	public BbpsOrder saveInit(BbpsOrder bbpsOrder) {
		Timestamp now = Timestamp.now();
		bbpsOrder.setCreateTime(now);
		bbpsOrder.setUpdateTime(now);
		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.INIT);
		return bbpsOrderRepository.save(bbpsOrder);
	}
	
	public BbpsOrder saveRecharging(BbpsOrder bbpsOrder) {
		Timestamp now = Timestamp.now();
		bbpsOrder.setOrderGid(Generators.timeBasedEpochGenerator().generate().toString());
		bbpsOrder.setCreateTime(now);
		bbpsOrder.setUpdateTime(now);
		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.RECHARGING);
		return bbpsOrderRepository.save(bbpsOrder);
	}
	
	public BbpsOrder updateStatus(BbpsOrder bbpsOrder) {
		Timestamp now = Timestamp.now();
		bbpsOrder.setUpdateTime(now);
		return bbpsOrderRepository.save(bbpsOrder);
	}
	
	public BbpsOrder findById(String orderGid) {
		return bbpsOrderRepository.findById(orderGid).orElseThrow(() -> new DataNotFoundException());
	}
	
	public Optional<BbpsOrder> findByOrderGid(String orderGid) {
		return bbpsOrderRepository.findByOrderGid(orderGid);
	}
	
	public List<BbpsOrder> queryBbpsOrder(BbpsOrderQueryBo bo) {
		return bbpsOrderRepository.queryBbpsOrder(bo.getOrderGid(), bo.getUserGid());
	}
	
	public List<BbpsOrder> findByRefId(String refId) {
		return bbpsOrderRepository.findByRefId(refId);
	}
	
}
