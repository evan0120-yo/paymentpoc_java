package com.citrus.payCore.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payCore.enums.BbpsRechargeStatusEnum;
import com.citrus.payCore.model.BbpsRecharge;
import com.citrus.payCore.object.bo.BbpsRechargeQueryBo;
import com.citrus.payCore.repository.BbpsRechargeRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BbpsRechargeDao {

	private final BbpsRechargeRepository bbpsRechargeRepository;

	public BbpsRecharge saveInit(BbpsRecharge bbpsRecharge) {
		Timestamp now = Timestamp.now();
		bbpsRecharge.setCreateTime(now);
		bbpsRecharge.setUpdateTime(now);
		bbpsRecharge.setBbpsRechargeStatus(BbpsRechargeStatusEnum.INIT);
		return bbpsRechargeRepository.save(bbpsRecharge);
	}

	public BbpsRecharge saveRecharging(BbpsRecharge bbpsRecharge) {
		Timestamp now = Timestamp.now();
		bbpsRecharge.setRechargeGid(Generators.timeBasedEpochGenerator().generate().toString());
		bbpsRecharge.setCreateTime(now);
		bbpsRecharge.setUpdateTime(now);
		bbpsRecharge.setBbpsRechargeStatus(BbpsRechargeStatusEnum.RECHARGING);
		return bbpsRechargeRepository.save(bbpsRecharge);
	}

	public BbpsRecharge updateStatus(BbpsRecharge bbpsRecharge) {
		Timestamp now = Timestamp.now();
		bbpsRecharge.setUpdateTime(now);
		return bbpsRechargeRepository.save(bbpsRecharge);
	}

	public BbpsRecharge findById(String rechargeGid) {
		return bbpsRechargeRepository.findById(rechargeGid).orElseThrow(() -> new DataNotFoundException());
	}

	public BbpsRecharge findByOrderGid(String orderGid) {
		List<BbpsRecharge> bbpsRechargeList = bbpsRechargeRepository.findByOrderGid(orderGid);
		if (bbpsRechargeList.isEmpty()) {
			throw new DataNotFoundException();
		}
		return bbpsRechargeList.get(0);
	}

	public List<BbpsRecharge> queryBbpsRecharge(BbpsRechargeQueryBo bo) {
		return bbpsRechargeRepository.queryBbpsRecharge(bo.getOrderGid(), bo.getRechargeGid());
	}
}
