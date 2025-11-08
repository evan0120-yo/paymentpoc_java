package com.citrus.payin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinStatusEnum {

	INIT,
	PENDING,
	CONFIRMING,
	SUCCESS,
	FAILED,
	
	;
}
