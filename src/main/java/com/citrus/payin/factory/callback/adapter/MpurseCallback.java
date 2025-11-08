package com.citrus.payin.factory.callback.adapter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.citrus.payin.factory.callback.PayinCallbackEnum;
import com.citrus.payin.factory.callback.PayinCallbackFactory;
import com.citrus.payin.factory.callback.object.bo.CallbackPaDataBo;
import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.object.dto.CallbackDto;
import com.citrus.payin.object.dto.SyncOrderDto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MpurseCallback implements PayinCallbackFactory {
	
	@Override
	public CallbackPaDto handleCallbackData(CallbackDto dto) {
		// TODO Auto-generated method stub
		// 模仿解析，這裡直接當作rawBody直接就是refId
		CallbackPaDataBo callbackPaDataBo = new com.google.gson.Gson()
		        .fromJson(dto.getRawBody(), CallbackPaDataBo.class);
		return CallbackPaDto.builder()
				.isSuccess(true)
				.refId(callbackPaDataBo.getRefId())
				.build();
	}

	@Override
	public CallbackPaDto syncOrderStatus(SyncOrderDto dto) {
		// TODO Auto-generated method stub
		// 模仿call甲方去確認訂單，這裡直接return結果
		// 1. 模擬 Headers (塞入一個 Map)
	    Map<String, String> headers = new HashMap<>();
	    headers.put("Content-Type", "application/json");
	    headers.put("X-Request-Id", "trace-" + dto.getRefId()); // 模擬追蹤 ID
	    headers.put("X-Mpurse-Signature", "sha256=a1b2c3d4e5f6..."); // 模擬簽章

	    // 2. 模擬 Raw Body (用 Map + Gson 生成 JSON 字串)
	    Map<String, Object> bodyMap = new HashMap<>();
	    bodyMap.put("transaction_id", dto.getRefId()); // 從傳入的 DTO 拿到 refId
	    bodyMap.put("status_code", "200");
	    bodyMap.put("status_message", "Transaction Successful");
	    bodyMap.put("amount", 199.00);
	    bodyMap.put("completed_at", Instant.now().toString());
	    
	    // 使用 Gson 將 Map 轉換為格式化的 JSON 字串
	    Gson gson = new GsonBuilder().setPrettyPrinting().create();
	    String rawBody = gson.toJson(bodyMap);
	    
	    // --- 假資料生成結束 ---


	    // 3. 組裝並回傳 CallbackPaDto
	    return CallbackPaDto.builder()
	        .isSuccess(true)
	        .refId(dto.getRefId())
	        .channel(PayinCallbackEnum.MPURSE) // 假設這是 Mpurse 的查詢結果
	        .rawBody(rawBody)
	        .headers(headers)
	        .build();
	}

}
