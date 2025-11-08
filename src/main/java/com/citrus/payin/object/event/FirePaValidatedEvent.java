package com.citrus.payin.object.event;

import java.math.BigDecimal;

import com.citrus.common.object.BasicEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class FirePaValidatedEvent extends BasicEvent {
	private String userGid;
	private String orderGid;
	private String rechargeGid;
	private String refId;
	private BigDecimal actualPaymentAmount;
	private BigDecimal billAmount;
	private String rechargeInfo;
}