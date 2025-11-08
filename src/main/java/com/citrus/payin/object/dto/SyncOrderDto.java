package com.citrus.payin.object.dto;

import com.citrus.payin.factory.callback.PayinCallbackEnum;
import com.citrus.payin.object.bo.PayinBo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SyncOrderDto {
	private String refId;
	private PayinCallbackEnum callbackChannel;
	private PayinBo payinBo;
}
