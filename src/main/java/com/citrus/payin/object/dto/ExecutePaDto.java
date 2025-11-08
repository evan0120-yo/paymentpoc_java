package com.citrus.payin.object.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExecutePaDto {
	private String orderGid;
	private String rechargeGid;
	private String userGid;
	private String billGid;
	private String refId;
	private BigDecimal actualPaymentAmount;
	private String rechargeInfo;
}
