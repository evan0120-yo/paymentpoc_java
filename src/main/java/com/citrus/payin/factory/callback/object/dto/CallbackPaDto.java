package com.citrus.payin.factory.callback.object.dto;

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
public class CallbackPaDto {
	private Boolean isSuccess;	// callback 結果
	private String refId;
	private PayinCallbackEnum channel;
	private String rawBody;
	private Map<String,String> headers;
}
