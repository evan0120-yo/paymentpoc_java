package com.citrus.payin.schedule;

import java.util.List;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Component;

import com.citrus.common.publisher.abs.EventPublisherAbs;
import com.citrus.payin.enums.PayinEventEnum;

@Component
public class PayinEventPublisher extends EventPublisherAbs {

	public PayinEventPublisher(RocketMQTemplate rocketMQTemplate) {
		super(rocketMQTemplate);
	}

	@Override
	public List<String> getSupportedTypes() {
		return PayinEventEnum.getAllTypeNames();
	}

}
