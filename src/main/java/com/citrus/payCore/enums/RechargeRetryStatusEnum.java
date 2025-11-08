package com.citrus.payCore.enums;

import com.citrus.common.enums.TaskStatus;

public enum RechargeRetryStatusEnum implements TaskStatus {

	PENDING,
	PROCESSING,
	FAIL,
	SUCCESS,
		
	;
}
