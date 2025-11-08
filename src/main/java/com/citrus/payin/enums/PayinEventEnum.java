package com.citrus.payin.enums;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PayinEventEnum {

	RECHARGE_INIT,
	CALLBACK_SUCCESSED,
	CALLBACK_FAILED,
	
    ;
	
	public static List<String> getAllTypeNames(){
		return Arrays.stream(PayinEventEnum.values())
				.map(PayinEventEnum::name)
				.toList();
	}
}
