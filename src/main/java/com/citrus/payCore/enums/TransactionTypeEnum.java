package com.citrus.payCore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TransactionTypeEnum {
	INIT("init"),								
	PAYMENT("recharging"),			
	REFUND("recharge failure"),
    
    ;

    private final String desc;

    public static TransactionTypeEnum fromCode(String desc) {
        for (TransactionTypeEnum status : values()) {
            if (status.desc.equals(desc)) {
                return status;
            }
        }
        throw new IllegalArgumentException("無效的 TransactionTypeEnum desc: " + desc);
    }
}
