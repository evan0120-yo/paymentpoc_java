package com.citrus.payin.schedule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.publisher.abs.EventPublisherAbs;
import com.citrus.payin.enums.PayinEventEnum;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;

@Component
public class PayinEventPublisher extends EventPublisherAbs {

	public PayinEventPublisher(PubSubPublisherTemplate publisherTemplate) {
		super(publisherTemplate);
	}

	@Override
	public List<String> getSupportedTypes() {
		// TODO Auto-generated method stub
		return PayinEventEnum.getAllTypeNames();
	}

}
