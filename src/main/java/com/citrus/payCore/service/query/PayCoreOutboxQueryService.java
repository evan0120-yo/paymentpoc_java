package com.citrus.payCore.service.query;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.payCore.dao.PayCoreOutboxDao;
import com.citrus.payCore.model.PayCoreOutbox;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreOutboxQueryService {
	
	private final PayCoreOutboxDao payCoreOutboxDao;
	
	public List<PayCoreOutbox> findInIds(List<String> payCoreOutboxIdList){
		List<PayCoreOutbox> returnList = new ArrayList<>();
		if(!payCoreOutboxIdList.isEmpty()) {
			returnList = payCoreOutboxDao.findInIds(payCoreOutboxIdList);
		}
		return returnList;
	}
	
	public List<PayCoreOutbox> findByInstanceId(String instanceId){
		return payCoreOutboxDao.findByInstanceId(instanceId, OutboxStatusEnum.PENDING);
	}
	
}