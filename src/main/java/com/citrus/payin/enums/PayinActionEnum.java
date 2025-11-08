package com.citrus.payin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinActionEnum {
	
	INITIATE_PAYMENT_SUCCESS,
	INITIATE_PAYMENT_FAIL,
	CALLBACK,
	STATUS_QUERY,
	
	;

}
