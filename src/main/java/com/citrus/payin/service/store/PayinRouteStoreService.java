package com.citrus.payin.service.store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.payin.dao.PayinAttemptDao;
import com.citrus.payin.dao.PayinLogDao;
import com.citrus.payin.dao.PayinRecordDao;
import com.citrus.payin.enums.PayinActionEnum;
import com.citrus.payin.enums.PayinAttemptStatusEnum;
import com.citrus.payin.enums.PayinDirectionEnum;
import com.citrus.payin.factory.channel.object.dto.PaPaymentDto;
import com.citrus.payin.factory.channel.object.dto.SavePaPayinDto;
import com.citrus.payin.model.PayinAttempt;
import com.citrus.payin.model.PayinLog;
import com.citrus.payin.model.PayinRecord;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinRouteStoreService {
	
	private final PayinRecordDao payinRecordDao;
	private final PayinAttemptDao payinAttemptDao;
	private final PayinLogDao payinLogDao;

	@Transactional("jpaTxManager")
	public PayinRecord savePaPayinBo(SavePaPayinDto savePaPayinDto) {
		PaPaymentDto paPaymentDto = savePaPayinDto.getPaPaymentDto();
		PayinRecord payinRecord = savePaPayinDto.getPayinRecord();
		PayinAttemptStatusEnum payinAttemptStatus = PayinAttemptStatusEnum.INITIATE_PAYMENT_FAIL;
		if(paPaymentDto.getIsSuccess()) {
			payinAttemptStatus = PayinAttemptStatusEnum.INITIATE_PAYMENT_SUCCESS;
		}
		// 1. save record
		if(payinRecord == null) {
			// 代表第一次沒有record單
			payinRecord = PayinRecord.builder()
					.orderGid(savePaPayinDto.getOrderGid())
					.rechargeGid(savePaPayinDto.getRechargeGid())
					.refId(savePaPayinDto.getRefId())
					.finalChannelName(paPaymentDto.getChannelName())
					.finalChannelTxnId(paPaymentDto.getChannelTxnId())
					.amount(paPaymentDto.getAmount())
					.currency(paPaymentDto.getCurrency())
					.build();
			payinRecord = payinRecordDao.save(payinRecord);
		} else {
			// 第二次以上測試 這裡要更新record單
			payinRecord.setFinalChannelName(paPaymentDto.getChannelName());
			payinRecord.setFinalChannelTxnId(paPaymentDto.getChannelTxnId());
			payinRecord = payinRecordDao.update(payinRecord);
		}
		// 2. save attempt
		PayinAttempt payinAttempt = PayinAttempt.builder()
				.payinRecordId(payinRecord.getPayinRecordId())
				.orderGid(savePaPayinDto.getOrderGid())
				.rechargeGid(savePaPayinDto.getRechargeGid())
				.refId(savePaPayinDto.getRefId())
				.payinAttemptStatus(payinAttemptStatus)
				.channelName(paPaymentDto.getChannelName())
				.channelTxId(paPaymentDto.getChannelTxnId())
				.amount(paPaymentDto.getAmount())
				.currency(paPaymentDto.getCurrency())
				.channelReqPayload(paPaymentDto.getPayloadReq())
				.channelRespPayload(paPaymentDto.getPayloadResp())
				.headersReq(paPaymentDto.getHeaderReq())
				.build();
		payinAttempt = payinAttemptDao.save(payinAttempt);
		// 3. save log req
		PayinLog payinLogReq = PayinLog.builder()
				.attemptId(payinAttempt.getPayinAttemptId())
				.orderGid(savePaPayinDto.getOrderGid())
				.rechargeGid(savePaPayinDto.getRechargeGid())
				.refId(savePaPayinDto.getRefId())
				.payinRecordId(payinRecord.getPayinRecordId())
				.payinAction(PayinActionEnum.INITIATE_PAYMENT_SUCCESS)
				.payinDirection(PayinDirectionEnum.REQ)
				.payload(paPaymentDto.getPayloadReq())
				.header(paPaymentDto.getHeaderReq())
				.build();
		payinLogDao.save(payinLogReq);
		// 4. save log resp
		PayinLog payinLogResp = PayinLog.builder()
				.attemptId(payinAttempt.getPayinAttemptId())
				.orderGid(savePaPayinDto.getOrderGid())
				.rechargeGid(savePaPayinDto.getRechargeGid())
				.refId(savePaPayinDto.getRefId())
				.payinRecordId(payinRecord.getPayinRecordId())
				.payinAction(PayinActionEnum.INITIATE_PAYMENT_SUCCESS)
				.payinDirection(PayinDirectionEnum.RESP)
				.payload(paPaymentDto.getPayloadResp())
				.header(paPaymentDto.getHeaderResp())
				.build();
		payinLogDao.save(payinLogResp);
		return payinRecord;
	}
	
}
