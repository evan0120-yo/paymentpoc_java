package com.citrus.payin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinAttemptStatusEnum {

	INITIATE_PAYMENT_SUCCESS,
	INITIATE_PAYMENT_FAIL,
	CONFIRMING,
	SUCCESS,
	FAILED,
	
	;
}
