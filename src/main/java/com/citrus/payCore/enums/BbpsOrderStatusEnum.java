package com.citrus.payCore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BbpsOrderStatusEnum {
	
	INIT("init"),								
    RECHARGING("recharging"),				// 充值中
    RECHARGE_FAILURE("recharge failure"),	// 充值失敗
	RECHARGED("recharged"),					// 充值完成
	PAYING("paying"),						// 支付中
	PAYMENT_FAILURE("payment failure"),		// 支付失敗
	PAID("paid"),							// 支付完成
    
    ;

    private final String desc;

    public static BbpsOrderStatusEnum fromCode(String desc) {
        for (BbpsOrderStatusEnum status : values()) {
            if (status.desc.equals(desc)) {
                return status;
            }
        }
        throw new IllegalArgumentException("無效的 BbpsOrderStatusEnum desc: " + desc);
    }
}
