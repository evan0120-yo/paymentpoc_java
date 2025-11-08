package com.citrus.payin.factory.channel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinChannelEnum {
	
	MPURSE("mpurseChannel"),
	BILLDESK("billdeskChannel"),
	
	;
	private final String className;
}
