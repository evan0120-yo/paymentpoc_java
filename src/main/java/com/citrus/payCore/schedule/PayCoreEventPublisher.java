package com.citrus.payCore.schedule;

import java.util.List;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import com.citrus.common.publisher.abs.EventPublisherAbs;
import com.citrus.payCore.enums.PayCoreEventEnum;

@Component
public class PayCoreEventPublisher extends EventPublisherAbs {

	public PayCoreEventPublisher(RocketMQTemplate rocketMQTemplate) {
		super(rocketMQTemplate);
	}

	@Override
	public List<String> getSupportedTypes() {
		return PayCoreEventEnum.getAllTypeNames();
	}
}
