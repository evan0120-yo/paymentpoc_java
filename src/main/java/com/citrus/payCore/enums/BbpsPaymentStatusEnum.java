package com.citrus.payCore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BbpsPaymentStatusEnum {

	INIT("init"),
	CANCEL("cancel"),					// 取消
    PAYING("paying"),					// 支付中
    PAID("paid"),						// 成功
    PAYMENT_FAILURE("payment failure"),	// 失敗
    
    ;

    private final String desc;

    public static BbpsPaymentStatusEnum fromCode(String desc) {
        for (BbpsPaymentStatusEnum status : values()) {
            if (status.desc.equals(desc)) {
                return status;
            }
        }
        throw new IllegalArgumentException("無效的 BbpsPaymentStatusEnum desc: " + desc);
    }
}
