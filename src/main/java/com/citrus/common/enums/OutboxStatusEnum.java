package com.citrus.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OutboxStatusEnum implements TaskStatus {
	
	PENDING,
	PROCESSING,
	SUCCESS,
	FAIL,
	
	;
}
