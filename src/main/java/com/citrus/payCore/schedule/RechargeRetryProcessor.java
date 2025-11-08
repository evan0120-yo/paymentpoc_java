package com.citrus.payCore.schedule;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.citrus.common.exception.DataErrorException;
import com.citrus.common.task.TaskProcessor;
import com.citrus.payCore.enums.RechargeRetryStatusEnum;
import com.citrus.payCore.model.BbpsOrder;
import com.citrus.payCore.model.RechargeRetry;
import com.citrus.payCore.object.dto.PayCoreRechargeSuccessDto;
import com.citrus.payCore.object.req.RechargeRetrySaveReq;
import com.citrus.payCore.repository.RechargeRetryRepository;
import com.citrus.payCore.service.query.PayCoreQueryService;
import com.citrus.payCore.usecase.store.PayCoreStoreUsecase;
import com.citrus.payCore.usecase.store.RechargeRetryStoreUsecase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RechargeRetryProcessor implements TaskProcessor<RechargeRetry> {
	
    private final RechargeRetryStoreUsecase rechargeRetryStoreUsecase;
    private final PayCoreStoreUsecase payCoreStoreUsecase;
    private final PayCoreQueryService payCoreQueryService;
    
	@Override
	public void process(RechargeRetry task) {
		// TODO Auto-generated method stub
		List<BbpsOrder> orderList = payCoreQueryService.findByRefId(task.getRefId());
		if(orderList.isEmpty()) {
			int currentRetry = task.getRetryCount();
			if(currentRetry >= 10) {
				task.setStatus(RechargeRetryStatusEnum.FAIL);
			} else {
				task.setRetryCount(currentRetry + 1);
				task.setStatus(RechargeRetryStatusEnum.PENDING);
			}
			rechargeRetryStoreUsecase.update(task);
		} else if (orderList.size() == 1){
			BbpsOrder order = orderList.get(0);
			System.out.println("Found matching BbpsOrder for refId: " + task.getRefId() + ". Applying success status.");
			
			PayCoreRechargeSuccessDto dto = PayCoreRechargeSuccessDto.builder()
					.refId(order.getRefId())
					.build();
			payCoreStoreUsecase.handleRechargeSuccess(dto);
			rechargeRetryStoreUsecase.delete(task);
		} else {
			throw new DataErrorException("ref找到超過一個結果, refId:"+task.getRefId());
		}
	}
	
	@Override
	public List<String> getSupportedTypes() {
		// TODO Auto-generated method stub
		return List.of("RECHARGE_RETRY");
	}
}
