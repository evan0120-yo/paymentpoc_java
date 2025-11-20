package com.citrus.payCore.service.query;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.citrus.payCore.dao.BbpsOrderDao;
import com.citrus.payCore.dao.BbpsPaymentDao;
import com.citrus.payCore.dao.BbpsRechargeDao;
import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.model.BbpsPayment;
import com.citrus.payCore.model.BbpsRecharge;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreQueryService {

	private final BbpsOrderDao bbpsOrderDao;
	private final BbpsPaymentDao bbpsPaymentDao;
	private final BbpsRechargeDao bbpsRechargeDao;

	public List<BbpsOrder> findOrderByRefId(String refId) {
		List<BbpsOrder> bbpsOrderList = bbpsOrderDao.findByRefId(refId);
		return bbpsOrderList;
	}

	public BbpsRecharge findRechargeByOrderGid(String orderGid) {
		return bbpsRechargeDao.findByOrderGid(orderGid);
	}

	public BbpsPayment findPaymentByOrderGid(String orderGid) {
		return bbpsPaymentDao.findByOrderGid(orderGid);
	}

	public Optional<BbpsOrder> findByOrderGid(String orderGid) {
		return bbpsOrderDao.findByOrderGid(orderGid);
	}

	public List<BbpsOrder> findByRefId(String refId) {
		return bbpsOrderDao.findByRefId(refId);
	}

}
