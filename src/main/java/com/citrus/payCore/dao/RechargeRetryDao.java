package com.citrus.payCore.dao;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.citrus.payCore.enums.RechargeRetryStatusEnum;
import com.citrus.payCore.model.RechargeRetry;
import com.citrus.payCore.repository.RechargeRetryRepository;
import com.fasterxml.uuid.Generators;
import com.google.cloud.Timestamp;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RechargeRetryDao {

	private final RechargeRetryRepository rechargeRetryRepository;

	public RechargeRetry save(RechargeRetry rechargeRetry) {
		Timestamp now = Timestamp.now();
		rechargeRetry.setRechargeRetryId(Generators.timeBasedEpochGenerator().generate().toString());
		rechargeRetry.setCreateTime(now);
		rechargeRetry.setUpdateTime(now);
		rechargeRetry.setRetryCount(0);
		rechargeRetry.setStatus(RechargeRetryStatusEnum.PENDING);
		rechargeRetryRepository.save(rechargeRetry);
		return rechargeRetry;
	}

	public RechargeRetry update(RechargeRetry rechargeRetry) {
		Timestamp now = Timestamp.now();
		rechargeRetry.setUpdateTime(now);
		rechargeRetryRepository.save(rechargeRetry);
		return rechargeRetry;
	}

	public void delete(RechargeRetry rechargeRetry) {
		rechargeRetryRepository.delete(rechargeRetry);
	}

	public Optional<RechargeRetry> findByRefId(String refId) {
		return rechargeRetryRepository.findByRefId(refId);
	}
}
