package com.citrus.share.enums;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TopicEnum {

    INIT_EVENT(
    		"payin-init-topic",
    		Map.of("PAYCORE", "paycore-init-group")
    ),

    CALLBACK_SUCCESS(
    		"payin-callback-success-topic",
    		Map.of("PAYCORE", "paycore-callback-success-group"
    )),

    RECHARGE_SUCCESS(
    		"paycore-recharge-success-topic",
    		Map.of("PAYLEDGER", "payledger-recharge-success-group",
    				"BILLER", "biller-recharge-success-group"
    )),

    RECHARGE_FAIL(
    		"paycore-recharge-fail-topic",
    		Map.of("PAYCORE", "paycore-recharge-fail-group"
    )),

    ;

    private final String topicId;
    private final Map<String, String> consumerGroupMap;
}
