package com.citrus.payCore.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BbpsRefundStatusEnum {

	INIT("init"),
    PROCESSING("processing"),	// 退款中
	SUCCESS("success"),			// 退款完成
    FAILURE("failure"),			// 退款失敗
    
    ;

    private final String desc;

    public static BbpsRefundStatusEnum fromCode(String desc) {
        for (BbpsRefundStatusEnum status : values()) {
            if (status.desc.equals(desc)) {
                return status;
            }
        }
        throw new IllegalArgumentException("無效的 BbpsRefundStatusEnum desc: " + desc);
    }
}
