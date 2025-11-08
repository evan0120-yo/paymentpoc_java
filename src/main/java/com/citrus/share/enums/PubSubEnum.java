package com.citrus.share.enums;

import java.util.Map;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PubSubEnum {
	
    INIT_EVENT(
    		"payin-init-topic", 
    		Map.of("PAYCORE", "paycore-init-subscription")
    ),
    
    CALLBACK_SUCCESS(
    		"payin-callback-success-topic", 
    		Map.of("PAYCORE", "paycore-callback-success-subscription"
    )),

    RECHARGE_SUCCESS(
    		"paycore-recharge-success-topic", 
    		Map.of("PAYLEDGER", "payledger-recharge-success-subscription", 
    				"BILLER", "biller-recharge-success-subscription"
    )),
    
    RECHARGE_FAIL(
    		"paycore-recharge-fail-topic", 
    		Map.of("PAYCORE", "paycore-recharge-fail-subscription"
    )),
    
    ;

    private final String topicId;
    private final Map<String, String> subscriptionMap;
}
