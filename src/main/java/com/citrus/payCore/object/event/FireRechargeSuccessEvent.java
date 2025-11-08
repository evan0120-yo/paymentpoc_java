package com.citrus.payCore.object.event;

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
public class FireRechargeSuccessEvent extends BasicEvent {
    private String orderGid;
    private String refId;
    private BigDecimal actualPaymentAmount;
    private BigDecimal billAmount;
    private String rechargeInfo;
}
