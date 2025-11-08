package com.citrus.payin.usecase.store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.payin.event.PayinEvent;
import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.object.bo.PayinBo;
import com.citrus.payin.object.dto.SyncOrderDto;
import com.citrus.payin.object.event.FireCallbackFailedEvent;
import com.citrus.payin.object.event.FireCallbackSucceededEvent;
import com.citrus.payin.service.query.PayinQueryService;
import com.citrus.payin.service.store.PayinStoreService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CallbackPaUsecase {
	
	private final PayinStoreService payinStoreService;
	private final PayinQueryService payinQueryService;
	private final PayinEvent payinEvent;
	
	@Transactional
	public PayinBo updatePaCallbackPreCheck(CallbackPaDto dto) {
		// 1. findpayin record and attempt
		PayinBo payinBo = payinQueryService.queryPayinBo(dto.getRefId());
		if(!payinBo.hasOrder()) {
			// 2-1. save retry callback
			FireCallbackFailedEvent event = FireCallbackFailedEvent.builder()
					.refId(dto.getRefId())
					.channel(dto.getChannel())
					.rawBody(dto.getRawBody())
					.headers(dto.getHeaders())
					.build();
			payinEvent.fireCallbackFailed(event);
			// 2-2. return
			return payinBo;
		}
		// 3. update payin record and attempt
		payinBo = payinStoreService.updatePaCallbackPreCheckBo(payinBo);
		// 4. insert log
		return payinStoreService.savePaCallbackLog(payinBo, dto);
	}

	@Transactional
	public void syncOrderStatus(SyncOrderDto syncOrderDto, CallbackPaDto callbackPaDto) {
		// 1. find and guard payin bo
		PayinBo payinBo = payinQueryService.querySyncOrder(syncOrderDto);
		// 2. update payin record
		payinBo = payinStoreService.updatePaCallbackBo(payinBo, callbackPaDto);
		// 3. publish > save outbox
		FireCallbackSucceededEvent event = FireCallbackSucceededEvent.builder()
				.refId(syncOrderDto.getRefId())
				.build();
		payinEvent.fireCallbackSucceeded(event);
	}
}
