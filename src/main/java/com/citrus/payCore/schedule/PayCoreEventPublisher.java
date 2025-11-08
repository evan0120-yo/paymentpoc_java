package com.citrus.payCore.schedule;

import java.util.List;

import org.springframework.stereotype.Component;

import com.citrus.common.publisher.abs.EventPublisherAbs;
import com.citrus.payCore.enums.PayCoreEventEnum;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;

@Component
public class PayCoreEventPublisher extends EventPublisherAbs {

	public PayCoreEventPublisher(PubSubPublisherTemplate publisherTemplate) {
		super(publisherTemplate);
	}
	
	@Override
	public List<String> getSupportedTypes() {
		// TODO Auto-generated method stub
		return PayCoreEventEnum.getAllTypeNames();
	}
}
