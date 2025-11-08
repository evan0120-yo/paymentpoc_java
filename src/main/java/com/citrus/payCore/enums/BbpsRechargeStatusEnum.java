package com.citrus.payCore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BbpsRechargeStatusEnum {

	INIT("init"),
    RECHARGING("recharging"),				// 充值中
    RECHARGED("recharged"),					// 充值完成
    RECHARGE_FAILURE("recharge failure"),	// 充值失敗
    
    ;

    private final String desc;

    public static BbpsRechargeStatusEnum fromCode(String desc) {
        for (BbpsRechargeStatusEnum status : values()) {
            if (status.desc.equals(desc)) {
                return status;
            }
        }
        throw new IllegalArgumentException("無效的 BbpsRechargeStatusEnum desc: " + desc);
    }
}
