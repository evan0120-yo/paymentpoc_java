package com.citrus.payin.factory.callback;

import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.object.dto.CallbackDto;
import com.citrus.payin.object.dto.SyncOrderDto;

public interface PayinCallbackFactory {
	CallbackPaDto handleCallbackData(CallbackDto dto);
	CallbackPaDto syncOrderStatus(SyncOrderDto dto);
}
