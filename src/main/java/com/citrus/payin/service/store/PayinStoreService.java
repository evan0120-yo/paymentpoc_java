package com.citrus.payin.service.store;

import org.springframework.stereotype.Service;

import com.citrus.payin.dao.PayinAttemptDao;
import com.citrus.payin.dao.PayinLogDao;
import com.citrus.payin.dao.PayinRecordDao;
import com.citrus.payin.enums.PayinActionEnum;
import com.citrus.payin.enums.PayinAttemptStatusEnum;
import com.citrus.payin.enums.PayinDirectionEnum;
import com.citrus.payin.enums.PayinStatusEnum;
import com.citrus.payin.factory.callback.object.dto.CallbackPaDto;
import com.citrus.payin.model.PayinAttempt;
import com.citrus.payin.model.PayinLog;
import com.citrus.payin.model.PayinRecord;
import com.citrus.payin.object.bo.PayinBo;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinStoreService {

	private final PayinRecordDao payinRecordDao;
	private final PayinAttemptDao payinAttemptDao;
	private final PayinLogDao payinLogDao;
	
	public PayinBo updatePaCallbackPreCheckBo(PayinBo payinBo) {
		// update record
		PayinRecord payinRecord = payinBo.getPayinRecord();
		payinRecord.setPayinStatus(PayinStatusEnum.CONFIRMING);
		payinRecord = payinRecordDao.update(payinRecord);
		payinBo.setPayinRecord(payinRecord);
		// update attempt
		PayinAttempt payinAttempt = payinBo.getPayinAttempt();
		payinAttempt.setPayinAttemptStatus(PayinAttemptStatusEnum.CONFIRMING);
		payinAttempt = payinAttemptDao.update(payinAttempt);
		payinBo.setPayinAttempt(payinAttempt);
		return payinBo;
	}
	
	public PayinBo updatePaCallbackBo(PayinBo payinBo, CallbackPaDto callbackPaDto) {
		PayinAttemptStatusEnum payinAttemptStatus = PayinAttemptStatusEnum.SUCCESS;
		PayinStatusEnum payinStatus = PayinStatusEnum.SUCCESS;
		PayinRecord payinRecord = payinBo.getPayinRecord();
		if(!callbackPaDto.getIsSuccess()) {
			payinStatus = PayinStatusEnum.FAILED;
			payinAttemptStatus = PayinAttemptStatusEnum.FAILED;
		}
		// update record
		payinRecord.setPayinStatus(payinStatus);
		payinRecordDao.update(payinRecord);
		payinBo.setPayinRecord(payinRecord);
		// update attempt
		PayinAttempt payinAttempt = payinBo.getPayinAttempt();
		payinAttempt.setPayinAttemptStatus(payinAttemptStatus);
		payinAttempt = payinAttemptDao.update(payinAttempt);
		payinBo.setPayinAttempt(payinAttempt);
		return payinBo;
	}
	
	public PayinBo savePaCallbackLog(PayinBo payinBo, CallbackPaDto callbackPaDto) {
		PayinActionEnum payinAction = PayinActionEnum.INITIATE_PAYMENT_FAIL;
		if(callbackPaDto.getIsSuccess()) {
			payinAction = PayinActionEnum.INITIATE_PAYMENT_SUCCESS;
		}
		Gson gson = new Gson();
		String jsonHeader = gson.toJson(callbackPaDto.getHeaders());
		
		PayinRecord payinRecord = payinBo.getPayinRecord();
		PayinAttempt payinAttempt = payinBo.getPayinAttempt();
		PayinLog payinLog = PayinLog.builder()
				.payinRecordId(payinRecord.getPayinRecordId())
				.attemptId(payinAttempt.getPayinAttemptId())
				.orderGid(payinRecord.getOrderGid())
				.rechargeGid(payinRecord.getRechargeGid())
				.refId(payinRecord.getRefId())
				.payinAction(payinAction)
				.payinDirection(PayinDirectionEnum.CALLBACK)
				.payload(callbackPaDto.getRawBody())
				.header(jsonHeader)
				.build();
		payinLog = payinLogDao.save(payinLog);
		payinBo.setPayinLog(payinLog);
		return payinBo;
	}
}
