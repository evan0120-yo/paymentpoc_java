package com.citrus.payCore.object.bo;

import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.model.BbpsPayment;
import com.citrus.payCore.model.BbpsRecharge;
import com.citrus.payCore.model.BbpsRefund;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayCoreBo {
	private BbpsOrder bbpsOrder;
	private BbpsPayment bbpsPayment;
	private BbpsRecharge bbpsRecharge;
	private BbpsRefund bbpsRefund;
}
