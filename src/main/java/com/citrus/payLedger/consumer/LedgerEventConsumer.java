package com.citrus.payLedger.consumer;

import java.nio.charset.StandardCharsets;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

@Component
@RocketMQMessageListener(
		topic = "paycore-recharge-success-topic",
		consumerGroup = "payledger-recharge-success-group",
		selectorExpression = "*",
		consumeMode = ConsumeMode.CONCURRENTLY,
		messageModel = MessageModel.CLUSTERING
)
public class LedgerEventConsumer implements RocketMQListener<MessageExt> {

	@Override
	public void onMessage(MessageExt msg) {
		String jsonPayload = new String(msg.getBody(), StandardCharsets.UTF_8);
		System.out.println("==========> 💰 PayLedger 收到新事件! <==========");
		System.out.println("訊息屬性 (tag/keys): tag=" + msg.getTags() + ", keys=" + msg.getKeys()
				+ ", aggregateType=" + msg.getUserProperty("aggregateType")
				+ ", outboxId=" + msg.getUserProperty("outboxId"));
		System.out.println("事件內容 (Payload): " + jsonPayload);

		// TODO: 驗證 properties、反序列化 payload、呼叫 use case 等
		System.out.println("...訊息業務邏輯處理成功...");
	}
}
