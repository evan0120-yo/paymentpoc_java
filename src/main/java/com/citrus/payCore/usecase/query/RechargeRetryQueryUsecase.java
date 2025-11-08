package com.citrus.payCore.usecase.query;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.citrus.payCore.model.RechargeRetry;
import com.citrus.payCore.service.query.RechargeRetryQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeRetryQueryUsecase {

	private final RechargeRetryQueryService rechargeRetryQueryService;
	
	public Optional<RechargeRetry> findByRefId(String refId) {
		return rechargeRetryQueryService.findByRefId(refId);
	}
}
