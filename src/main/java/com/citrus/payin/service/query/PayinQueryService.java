package com.citrus.payin.service.query;

import java.util.List;

import org.springframework.stereotype.Service;

import com.citrus.common.exception.DataErrorException;
import com.citrus.common.exception.DataNotFoundException;
import com.citrus.payin.dao.PayinAttemptDao;
import com.citrus.payin.dao.PayinRecordDao;
import com.citrus.payin.enums.PayinAttemptStatusEnum;
import com.citrus.payin.enums.PayinStatusEnum;
import com.citrus.payin.model.PayinAttempt;
import com.citrus.payin.model.PayinRecord;
import com.citrus.payin.object.bo.PayinBo;
import com.citrus.payin.object.dto.SyncOrderDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinQueryService {

	private final PayinRecordDao payinRecordDao;
	private final PayinAttemptDao payinAttemptDao;
	
	public PayinBo queryPayinBo(String refId) {
		List<PayinRecord> payinRecordList = payinRecordDao.findByRefId(refId);
		if(payinRecordList.isEmpty()) {
			return PayinBo.builder().build();
		}
		PayinRecord payinRecord = payinRecordList.get(0);
		List<PayinAttempt> payinAttemptList = payinAttemptDao.findByRefIdWithPaSuccess(refId);
		if(payinAttemptList.isEmpty()) {
			throw new DataNotFoundException();
		}
		PayinAttempt payinAttempt = payinAttemptList.get(0);
		return PayinBo.builder()
				.payinRecord(payinRecord)
				.payinAttempt(payinAttempt)
				.build();
	}
	
	public PayinBo querySyncOrder(SyncOrderDto dto) {
		PayinBo payinBo = dto.getPayinBo();
		// 1. get payinBo
		if(payinBo == null) {
			if(dto.getRefId() == null) {
				
				throw new DataErrorException();
			} else {
				payinBo = queryPayinBo(dto.getRefId());
			}
		}
		// 2. guard status
		PayinRecord payinRecord = payinBo.getPayinRecord();
		if(!(payinRecord.getPayinStatus().equals(PayinStatusEnum.PENDING) || payinRecord.getPayinStatus().equals(PayinStatusEnum.CONFIRMING))) {
			throw new DataErrorException("record狀態不對");
		}
		PayinAttempt payinAttempt = payinBo.getPayinAttempt();
		if(!(payinAttempt.getPayinAttemptStatus().equals(PayinAttemptStatusEnum.INITIATE_PAYMENT_SUCCESS) || payinAttempt.getPayinAttemptStatus().equals(PayinAttemptStatusEnum.CONFIRMING))) {
			throw new DataErrorException("attempt狀態不對");
		}
		return payinBo;
	}
}
