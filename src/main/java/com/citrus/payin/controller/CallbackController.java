package com.citrus.payin.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citrus.payin.factory.callback.PayinCallbackEnum;
import com.citrus.payin.object.dto.CallbackDto;
import com.citrus.payin.usecase.store.PayinPaStoreUsecase;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/callback")
@RequiredArgsConstructor
public class CallbackController {
	
	private final PayinPaStoreUsecase payinPaStoreUsecase;

	@PostMapping("/mpurse")
	public ResponseEntity<?> handleMpurseCallback(HttpServletRequest req) throws IOException { // 【修正】拿掉 @RequestBody

	    // 從 HttpServletRequest 中提取 Raw Body
	    String rawBody = req.getReader().lines().collect(Collectors.joining(System.lineSeparator()));

	    // 從 HttpServletRequest 提取所有 Headers
	    Map<String, String> headers = Collections.list(req.getHeaderNames())
	                                             .stream()
	                                             .collect(Collectors.toMap(
	                                                 headerName -> headerName,
	                                                 req::getHeader
	                                             ));
	    // 使用您原有的命名和邏輯，建立 DTO
	    CallbackDto dto = CallbackDto.builder()
	            .rawBody(rawBody)
	            .headers(headers)
	            .callbackChannel(PayinCallbackEnum.MPURSE)
	            .build();

	    // 呼叫 Usecase
	    payinPaStoreUsecase.handlePaCallback(dto);

	    return ResponseEntity.ok().build();
	}
}
