package com.citrus.common.publisher.abs;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.stereotype.Component;

import com.citrus.common.model.Outbox;
import com.citrus.common.publisher.EventPublisher;
import com.google.cloud.spring.pubsub.core.publisher.PubSubPublisherTemplate;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public abstract class EventPublisherAbs implements EventPublisher {

	protected final PubSubPublisherTemplate publisherTemplate;
	
	@Override
    public void process(Outbox outbox) {
		Gson gson = new Gson();
		// --- 準備「信封上的標籤」(Attributes Map) ---
	    Map<String, String> attributes = new HashMap<>();
	    attributes.put("aggregateType", outbox.getAggregateType());
	    attributes.put("eventType", outbox.getEventType());
	    attributes.put("id", outbox.getId());

	    // --- 準備「箱子裡的貨物」(Payload JSON String) ---
	    String jsonPayload = gson.toJson(outbox);
		CompletableFuture<String> future = publisherTemplate.publish(outbox.getTopicId(), jsonPayload, attributes);
        future.whenComplete((messageId, throwable) -> {
            if (throwable != null) {
                // 這裡相當於之前的 onFailure
            	System.out.println("發布事件到 GCP Pub/Sub 失敗！Outbox ID:"+outbox.getId()+", 原因: {}"+throwable.getMessage());
            } else {
                // 這裡相當於之前的 onSuccess
            	System.out.println("成功發布事件到 GCP Pub/Sub。Outbox ID: "+outbox.getId()+", Message ID: {}"+messageId);
            }
        });
    }
}
