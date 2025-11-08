package com.citrus.payCore.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayCoreEventEnum {

	RECHARGE_SUCCEEDED,
    RECHARGE_FAILED,
    
    ;
	
	public static List<String> getAllTypeNames(){
		return Arrays.stream(PayCoreEventEnum.values())
				.map(PayCoreEventEnum::name)
				.toList();
	}
}
