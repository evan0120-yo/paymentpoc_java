package com.citrus.payCore.service.store;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.payCore.dao.BbpsOrderDao;
import com.citrus.payCore.dao.BbpsPaymentDao;
import com.citrus.payCore.dao.BbpsRechargeDao;
import com.citrus.payCore.dao.BbpsRefundDao;
import com.citrus.payCore.enums.BbpsOrderStatusEnum;
import com.citrus.payCore.enums.BbpsPaymentStatusEnum;
import com.citrus.payCore.enums.BbpsRechargeStatusEnum;
import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.model.BbpsPayment;
import com.citrus.payCore.model.BbpsRecharge;
import com.citrus.payCore.object.bo.PayCoreBo;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreStoreService {
	
	private final BbpsOrderDao bbpsOrderDao;
	private final BbpsPaymentDao bbpsPaymentDao;
	private final BbpsRechargeDao bbpsRechargeDao;
	private final BbpsRefundDao bbpsRefundDao;

	@Transactional("jpaTxManager")
	public PayCoreBo createPaymentOrderInit(String orderGid, String rechargeGid, String userGid, String refId, BigDecimal actualPaymentAmount, BigDecimal billAmount, String rechargeInfo) {
		// 1. order -> create
		BbpsOrder bbpsOrder = BbpsOrder.builder()
				.orderGid(orderGid)
				.userGid(userGid)
				.refId(refId)
				.actualPaymentAmount(actualPaymentAmount)
				.billAmount(billAmount)
				.build();
		bbpsOrderDao.saveInit(bbpsOrder);
		// 2. payment -> create
		BbpsPayment bbpsPayment = BbpsPayment.builder()
				.orderGid(bbpsOrder.getOrderGid())
				.actualPaymentAmount(actualPaymentAmount)
				.build();
		bbpsPaymentDao.saveInit(bbpsPayment);
		// 3. recharge -> create
		BbpsRecharge bbpsRecharge = BbpsRecharge.builder()
				.orderGid(bbpsOrder.getOrderGid())
				.rechargeGid(rechargeGid)
				.rechargeInfo(rechargeInfo)
				
				.build();
		bbpsRechargeDao.saveInit(bbpsRecharge);
		return PayCoreBo.builder()
				.bbpsOrder(bbpsOrder)
				.bbpsPayment(bbpsPayment)
				.bbpsRecharge(bbpsRecharge)
				.build();
	}
	
//	@Transactional
//	public PayCoreBo createPaymentOrderRecharge(String userGid, String refId, BigDecimal actualPaymentAmount, BigDecimal billAmount, String rechargeInfo) {
//		// 1. order -> create
//		BbpsOrder bbpsOrder = BbpsOrder.builder()
//				.userGid(userGid)
//				.refId(refId)
//				.actualPaymentAmount(actualPaymentAmount)
//				.billAmount(billAmount)
//				.build();
//		bbpsOrderDao.saveRecharging(bbpsOrder);
//		// 2. payment -> create
//		BbpsPayment bbpsPayment = BbpsPayment.builder()
//				.orderGid(bbpsOrder.getOrderGid())
//				.actualPaymentAmount(actualPaymentAmount)
//				.build();
//		bbpsPaymentDao.saveInit(bbpsPayment);
//		// 3. recharge -> create
//		BbpsRecharge bbpsRecharge = BbpsRecharge.builder()
//				.orderGid(bbpsOrder.getOrderGid())
//				.rechargeInfo(rechargeInfo)
//				.build();
//		bbpsRechargeDao.saveRecharging(bbpsRecharge);
//		return PayCoreBo.builder()
//				.bbpsOrder(bbpsOrder)
//				.bbpsPayment(bbpsPayment)
//				.bbpsRecharge(bbpsRecharge)
//				.build();
//	}
	
//	@Transactional
//	public PayCoreBo processRecharge(PayCoreBo payCoreBo) {
//		// 1. update order
//		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
//		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.RECHARGING);
//		BbpsOrder bbpsOrderUpdated = bbpsOrderDao.updateStatus(bbpsOrder);
//		payCoreBo.setBbpsOrder(bbpsOrderUpdated);
//		// 2. update recharge
//		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
//		bbpsRecharge.setBbpsRechargeStatus(BbpsRechargeStatusEnum.RECHARGING);
//		BbpsRecharge bbpsRechargeUpdated = bbpsRechargeDao.updateStatus(bbpsRecharge);
//		payCoreBo.setBbpsRecharge(bbpsRechargeUpdated);
//		return payCoreBo;
//	}
	
	@Transactional("jpaTxManager")
	public PayCoreBo handleRechargeSuccess(PayCoreBo payCoreBo) {
		// 1. update order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.RECHARGED);
		BbpsOrder bbpsOrderUpdated = bbpsOrderDao.updateStatus(bbpsOrder);
		payCoreBo.setBbpsOrder(bbpsOrderUpdated);
		// 2. update recharge
		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
		bbpsRecharge.setBbpsRechargeStatus(BbpsRechargeStatusEnum.RECHARGED);
		BbpsRecharge bbpsRechargeUpdated = bbpsRechargeDao.updateStatus(bbpsRecharge);
		payCoreBo.setBbpsRecharge(bbpsRechargeUpdated);
		return payCoreBo;
	}
	
	@Transactional("jpaTxManager")
	public PayCoreBo handleRechargeFailure(PayCoreBo payCoreBo) {
		// 1. update order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.RECHARGE_FAILURE);
		BbpsOrder bbpsOrderUpdated = bbpsOrderDao.updateStatus(bbpsOrder);
		payCoreBo.setBbpsOrder(bbpsOrderUpdated);
		// 2. update recharge
		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
		bbpsRecharge.setBbpsRechargeStatus(BbpsRechargeStatusEnum.RECHARGE_FAILURE);
		BbpsRecharge bbpsRechargeUpdated = bbpsRechargeDao.updateStatus(bbpsRecharge);
		payCoreBo.setBbpsRecharge(bbpsRechargeUpdated);
		// 3. update payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		bbpsPayment.setBbpsPaymentStatus(BbpsPaymentStatusEnum.CANCEL);
		BbpsPayment bbpsPaymentUpdated = bbpsPaymentDao.updateStatus(bbpsPayment);
		payCoreBo.setBbpsPayment(bbpsPaymentUpdated);
		return payCoreBo;
	}
	
//	@Transactional
//	public PayCoreBo processBillPayment(PayCoreBo payCoreBo) {
//		// 1. update order
//		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
//		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.PAYING);
//		BbpsOrder bbpsOrderUpdated = bbpsOrderDao.updateStatus(bbpsOrder);
//		payCoreBo.setBbpsOrder(bbpsOrderUpdated);
//		// 2. update payment
//		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
//		bbpsPayment.setBbpsPaymentStatus(BbpsPaymentStatusEnum.PAYING);
//		BbpsPayment bbpsPaymentUpdated = bbpsPaymentDao.updateStatus(bbpsPayment);
//		payCoreBo.setBbpsPayment(bbpsPaymentUpdated);
//		return payCoreBo;
//	}
	
	@Transactional("jpaTxManager")
	public PayCoreBo handlePaymentSuccess(PayCoreBo payCoreBo) {
		// 1. update order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.PAID);
		BbpsOrder bbpsOrderUpdated = bbpsOrderDao.updateStatus(bbpsOrder);
		payCoreBo.setBbpsOrder(bbpsOrderUpdated);
		// 2. update payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		bbpsPayment.setBbpsPaymentStatus(BbpsPaymentStatusEnum.PAID);
		BbpsPayment bbpsPaymentUpdated = bbpsPaymentDao.updateStatus(bbpsPayment);
		payCoreBo.setBbpsPayment(bbpsPaymentUpdated);
		return payCoreBo;
	}
	
	@Transactional("jpaTxManager")
	public PayCoreBo handlePaymentFailure(PayCoreBo payCoreBo) {
		// 1. update order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		bbpsOrder.setBbpsOrderStatus(BbpsOrderStatusEnum.PAYMENT_FAILURE);
		BbpsOrder bbpsOrderUpdated = bbpsOrderDao.updateStatus(bbpsOrder);
		payCoreBo.setBbpsOrder(bbpsOrderUpdated);
		// 2. update payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		bbpsPayment.setBbpsPaymentStatus(BbpsPaymentStatusEnum.PAYMENT_FAILURE);
		BbpsPayment bbpsPaymentUpdated = bbpsPaymentDao.updateStatus(bbpsPayment);
		payCoreBo.setBbpsPayment(bbpsPaymentUpdated);
		return payCoreBo;
	}
	
}
