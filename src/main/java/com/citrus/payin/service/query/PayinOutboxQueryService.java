package com.citrus.payin.service.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.payin.dao.PayinOutboxDao;
import com.citrus.payin.model.PayinOutbox;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayinOutboxQueryService {

	private final PayinOutboxDao payinOutboxDao;
	
	public List<PayinOutbox> findInIds(List<String> payinOutboxIdList){
		List<PayinOutbox> returnList = new ArrayList<>();
		if(!payinOutboxIdList.isEmpty()) {
			returnList = payinOutboxDao.findInIds(payinOutboxIdList);
		}
		return returnList;
	}
	
	public List<PayinOutbox> findByInstanceId(String instanceId){
		return payinOutboxDao.findByInstanceId(instanceId, OutboxStatusEnum.PENDING);
	}
}
