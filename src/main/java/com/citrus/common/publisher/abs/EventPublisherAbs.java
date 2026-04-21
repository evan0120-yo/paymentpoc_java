package com.citrus.common.publisher.abs;

import java.util.HashMap;
import java.util.Map;

import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

import com.citrus.common.model.Outbox;
import com.citrus.common.publisher.EventPublisher;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public abstract class EventPublisherAbs implements EventPublisher {

	private static final long SEND_TIMEOUT_MS = 3000L;

	protected final RocketMQTemplate rocketMQTemplate;

	@Override
	public void process(Outbox outbox) {
		Gson gson = new Gson();
		// --- 準備 headers（對應 Pub/Sub 的 attributes）---
		Map<String, Object> headers = new HashMap<>();
		headers.put("aggregateType", outbox.getAggregateType());
		headers.put("outboxId", outbox.getId());
		// RocketMQ 用 KEYS 做訊息檢索鍵，對齊 outbox id 方便追查
		headers.put(org.apache.rocketmq.spring.support.RocketMQHeaders.KEYS, outbox.getId());

		// --- destination = topic:tag，tag 用 eventType 方便消費端過濾與觀測 ---
		String destination = outbox.getTopicId() + ":" + outbox.getEventType();

		// --- payload 走 JSON string，與原本 Pub/Sub payload 行為一致 ---
		String jsonPayload = gson.toJson(outbox);
		Message<String> message = MessageBuilder.withPayload(jsonPayload).copyHeaders(headers).build();

		SendResult result = rocketMQTemplate.syncSend(destination, message, SEND_TIMEOUT_MS);
		if (result == null || result.getSendStatus() != SendStatus.SEND_OK) {
			String status = result == null ? "null" : result.getSendStatus().name();
			System.err.println("發布事件到 RocketMQ 失敗！Outbox ID:" + outbox.getId() + ", status: " + status);
			throw new RuntimeException("Publish failed for Outbox ID: " + outbox.getId() + ", status: " + status);
		}
		System.out.println("成功發布事件到 RocketMQ。Outbox ID: " + outbox.getId() + ", Message ID: " + result.getMsgId());
	}
}
