package com.citrus.payin.service.query;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
		try(ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()){
			Future<List<PayinRecord>> payinRecordListFuture = executor.submit(() -> payinRecordDao.findByRefId(refId));
			Future<List<PayinAttempt>> payinAttemptListFuture = executor.submit(() -> payinAttemptDao.findByRefIdWithPaSuccess(refId));
			
			List<PayinRecord> payinRecordList = payinRecordListFuture.get();
			if(payinRecordList.isEmpty()) {
				return PayinBo.builder().build();
			}
			List<PayinAttempt> payinAttemptList = payinAttemptListFuture.get();
			if(payinAttemptList.isEmpty()) {
				return PayinBo.builder().build();
			}
			return PayinBo.builder()
					.payinRecord(payinRecordList.get(0))
					.payinAttempt(payinAttemptList.get(0))
					.build();
		} catch (Exception e) {
			System.out.println(e);
			throw new DataErrorException("並行查詢被中斷", e);
		}
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
