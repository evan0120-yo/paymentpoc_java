package com.citrus.payin.factory.callback;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinCallbackEnum {

	MPURSE("mpurseCallback"),
	BILLDESK("billdeskCallback"),
	
	;
	private final String className;
}
