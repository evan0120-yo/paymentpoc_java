package com.citrus.payCore.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.citrus.payCore.object.req.RechargeRetrySaveReq;
import com.citrus.payCore.usecase.store.RechargeRetryStoreUsecase;

import lombok.RequiredArgsConstructor;

@CrossOrigin(value = "*")
@RestController
@RequestMapping(value = "/bbps")
@RequiredArgsConstructor
public class BbpsController {
	
	private final RechargeRetryStoreUsecase rechargeRetryStoreUsecase;

	@PostMapping("/test")
	public ResponseEntity<?> test(@RequestBody RechargeRetrySaveReq req) {
		return ResponseEntity.ok(rechargeRetryStoreUsecase.save(req));
	}
}
