package com.citrus.payin.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citrus.payin.object.req.InitiatePaymentReq;
import com.citrus.payin.usecase.store.PayinPaStoreUsecase;

import lombok.RequiredArgsConstructor;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/payin")
@RequiredArgsConstructor
public class PayinController {

	private final PayinPaStoreUsecase payinPaStoreUsecase;

	@PostMapping("/pa")
	public ResponseEntity<?> initiatePaPayment(@RequestBody InitiatePaymentReq req) {
		return ResponseEntity.ok(payinPaStoreUsecase.initiatePaPayment(req));
	}
}
