package com.citrus.payCore.service.store;

import org.springframework.stereotype.Service;

import com.citrus.payCore.dao.RechargeRetryDao;
import com.citrus.payCore.model.RechargeRetry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeRetryStoreService {

	private final RechargeRetryDao rechargeRetryDao;
	
	public RechargeRetry save(String refId) {
		RechargeRetry rechargeRetry = RechargeRetry.builder()
				.refId(refId)
				.build();
		return rechargeRetryDao.save(rechargeRetry);
	}
	
	public RechargeRetry update(RechargeRetry rechargeRetry) {
		return rechargeRetryDao.update(rechargeRetry);
	}
	
	public void delete(RechargeRetry rechargeRetry) {
		rechargeRetryDao.delete(rechargeRetry);
	}
}
