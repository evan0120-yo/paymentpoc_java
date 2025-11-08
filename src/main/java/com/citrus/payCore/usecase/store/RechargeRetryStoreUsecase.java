package com.citrus.payCore.usecase.store;

import org.springframework.stereotype.Service;

import com.citrus.payCore.model.RechargeRetry;
import com.citrus.payCore.object.req.RechargeRetrySaveReq;
import com.citrus.payCore.service.store.RechargeRetryStoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeRetryStoreUsecase {

	private final RechargeRetryStoreService rechargeRetryStoreService;
	
	public RechargeRetry save(RechargeRetrySaveReq req) {
		return rechargeRetryStoreService.save(req.getRefId());
	}
	
	public RechargeRetry update(RechargeRetry rechargeRetry) {
		return rechargeRetryStoreService.update(rechargeRetry);
	}
	
	public void delete(RechargeRetry rechargeRetry) {
		rechargeRetryStoreService.delete(rechargeRetry);
	}
}
