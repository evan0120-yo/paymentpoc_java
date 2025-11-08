package com.citrus.payCore.service.guard;

import org.springframework.stereotype.Service;

import com.citrus.common.exception.DataErrorException;
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
public class PayCoreGuardService {

	public void processRechargeGuard(PayCoreBo payCoreBo) {
		// 1. guard order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		if(bbpsOrder == null || !bbpsOrder.getBbpsOrderStatus().equals(BbpsOrderStatusEnum.INIT)) {
			throw new DataErrorException();
		}
		// 2. guard recharge
		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
		if(bbpsRecharge == null || !bbpsRecharge.getBbpsRechargeStatus().equals(BbpsRechargeStatusEnum.INIT)) {
			throw new DataErrorException();
		}
	}
	
	public void handleRechargeSuccessGuard(PayCoreBo payCoreBo) {
		// 1. guard order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		if(bbpsOrder == null || !bbpsOrder.getBbpsOrderStatus().equals(BbpsOrderStatusEnum.INIT)) {
			throw new DataErrorException();
		}
		// 2. guard recharge
		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
		if(bbpsRecharge == null || !bbpsRecharge.getBbpsRechargeStatus().equals(BbpsRechargeStatusEnum.INIT)) {
			throw new DataErrorException();
		}
	}
	
	public void handleRechargeFailureGuard(PayCoreBo payCoreBo) {
		// 1. guard order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		if(bbpsOrder == null || !bbpsOrder.getBbpsOrderStatus().equals(BbpsOrderStatusEnum.INIT)) {
			throw new DataErrorException();
		}
		// 2. guard recharge
		BbpsRecharge bbpsRecharge = payCoreBo.getBbpsRecharge();
		if(bbpsRecharge == null || !bbpsRecharge.getBbpsRechargeStatus().equals(BbpsRechargeStatusEnum.INIT)) {
			throw new DataErrorException();
		}
		// 3. guard payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		if(bbpsPayment == null || bbpsPayment.getBbpsPaymentStatus().equals(BbpsPaymentStatusEnum.INIT)) {
			throw new DataErrorException();
		}
	}
	
	public void processBillPaymentGuard(PayCoreBo payCoreBo) {
		// 1. guard order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		if(bbpsOrder == null || !bbpsOrder.getBbpsOrderStatus().equals(BbpsOrderStatusEnum.RECHARGED)) {
			throw new DataErrorException();
		}
		// 2. guard payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		if(bbpsPayment == null || bbpsPayment.getBbpsPaymentStatus().equals(BbpsPaymentStatusEnum.INIT)) {
			throw new DataErrorException();
		}
	}
	
	public void handlePaymentSuccessGuard(PayCoreBo payCoreBo) {
		// 1. guard order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		if(bbpsOrder == null || !bbpsOrder.getBbpsOrderStatus().equals(BbpsOrderStatusEnum.RECHARGED)) {
			throw new DataErrorException();
		}
		// 2. guard payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		if(bbpsPayment == null || bbpsPayment.getBbpsPaymentStatus().equals(BbpsPaymentStatusEnum.INIT)) {
			throw new DataErrorException();
		}
	}
	
	public void handlePaymentFailureGuard(PayCoreBo payCoreBo) {
		// 1. guard order
		BbpsOrder bbpsOrder = payCoreBo.getBbpsOrder();
		if(bbpsOrder == null || !bbpsOrder.getBbpsOrderStatus().equals(BbpsOrderStatusEnum.RECHARGED)) {
			throw new DataErrorException();
		}
		// 2. guard payment
		BbpsPayment bbpsPayment = payCoreBo.getBbpsPayment();
		if(bbpsPayment == null || bbpsPayment.getBbpsPaymentStatus().equals(BbpsPaymentStatusEnum.INIT)) {
			throw new DataErrorException();
		}
	}
}
