package com.citrus.payCore.object.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayCoreInitDto {
	private String userGid;
    private String orderGid;
    private String rechargeGid;
	private String refId;
	private BigDecimal actualPaymentAmount;
	private BigDecimal billAmount;
	private String rechargeInfo;
}