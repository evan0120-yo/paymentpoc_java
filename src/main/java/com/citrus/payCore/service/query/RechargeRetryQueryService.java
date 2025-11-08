package com.citrus.payCore.service.query;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.citrus.payCore.dao.RechargeRetryDao;
import com.citrus.payCore.model.RechargeRetry;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RechargeRetryQueryService {

	private final RechargeRetryDao rechargeRetryDao;
	
	public Optional<RechargeRetry> findByRefId(String refId) {
		return rechargeRetryDao.findByRefId(refId);
	}
}
