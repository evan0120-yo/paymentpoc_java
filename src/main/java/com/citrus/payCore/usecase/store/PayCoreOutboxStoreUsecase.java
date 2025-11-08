package com.citrus.payCore.usecase.store;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.payCore.enums.PayCoreEventEnum;
import com.citrus.payCore.model.PayCoreOutbox;
import com.citrus.payCore.object.event.FireRechargeSuccessEvent;
import com.citrus.payCore.service.store.PayCoreOutboxStroeService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayCoreOutboxStoreUsecase {

	private final PayCoreOutboxStroeService payCoreOutboxStroeService;
	
	public PayCoreOutbox saveRechargeSuccess(FireRechargeSuccessEvent event) {
		return payCoreOutboxStroeService.saveRechargeSuccess(event, PayCoreEventEnum.RECHARGE_SUCCEEDED);
	}

	@Transactional
	public void claimPendingTasks(String instanceId) {
		// 1. update outbox
		payCoreOutboxStroeService.claimPendingTasks(instanceId);
	}
	
	@Transactional
	public void updateStatusByInstanceId(String instanceId) {
		// 1. update outbox
		payCoreOutboxStroeService.updateStatusByInstanceId(instanceId);
	}
}
