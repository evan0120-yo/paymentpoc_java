package com.citrus.payCore.usecase.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.model.BbpsPayment;
import com.citrus.payCore.model.BbpsRecharge;
import com.citrus.payCore.object.bo.PayCoreBo;
import com.citrus.payCore.service.query.PayCoreQueryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreQueryUsecase {

	private final PayCoreQueryService payCoreQueryService;
	
	public PayCoreBo queryPayCoreBo(String refId) {
		List<BbpsOrder> bbpsOrderList = payCoreQueryService.findOrderByRefId(refId);
		if(bbpsOrderList.isEmpty()) {
			// TODO 進入database循環驗證
			return PayCoreBo.builder().build();
		}
		BbpsOrder bbpsOrder = bbpsOrderList.get(0);
		BbpsRecharge bbpsRecharge = payCoreQueryService.findRechargeByOrderGid(bbpsOrder.getOrderGid());
		BbpsPayment bbpsPayment = payCoreQueryService.findPaymentByOrderGid(bbpsOrder.getOrderGid());
		return PayCoreBo.builder()
				.bbpsOrder(bbpsOrder)
				.bbpsRecharge(bbpsRecharge)
				.bbpsPayment(bbpsPayment)
				.build();
	}
}
