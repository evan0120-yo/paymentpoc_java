package com.citrus.payin.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinDirectionEnum {

	REQ,
	RESP,
	CALLBACK,
	
	;
}
