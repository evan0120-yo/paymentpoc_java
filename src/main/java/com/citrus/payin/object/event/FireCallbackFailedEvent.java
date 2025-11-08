package com.citrus.payin.object.event;

import java.util.Map;

import com.citrus.common.object.BasicEvent;
import com.citrus.payin.factory.callback.PayinCallbackEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class FireCallbackFailedEvent extends BasicEvent {
	private String refId;
	private PayinCallbackEnum channel;
	private String rawBody;
	private Map<String,String> headers;
}
