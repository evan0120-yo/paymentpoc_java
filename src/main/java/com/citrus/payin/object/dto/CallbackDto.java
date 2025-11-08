package com.citrus.payin.object.dto;

import java.util.Map;

import com.citrus.payin.factory.callback.PayinCallbackEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallbackDto {
	private String rawBody;
	private Map<String,String> headers;
	private PayinCallbackEnum callbackChannel;
}
