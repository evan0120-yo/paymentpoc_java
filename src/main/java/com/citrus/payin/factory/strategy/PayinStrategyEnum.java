package com.citrus.payin.factory.strategy;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinStrategyEnum {
	
	DEFAULT("defaultStrategy"),
	
	;
	private final String className;
}
