package com.citrus.payin.object.req;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitiatePaymentReq {
	private String userGid;
	private String billGid;
	private String refId;
	private BigDecimal actualPaymentAmount;
	private String rechargeInfo;
}
