package com.citrus.payCore.consumer;

import java.nio.charset.StandardCharsets;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import com.citrus.payCore.object.dto.PayCoreRechargeSuccessDto;
import com.citrus.payCore.usecase.store.PayCoreStoreUsecase;
import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
		topic = "payin-callback-success-topic",
		consumerGroup = "paycore-callback-success-group",
		selectorExpression = "*",
		consumeMode = ConsumeMode.CONCURRENTLY,
		messageModel = MessageModel.CLUSTERING
)
public class PaycoreCallbackSuccessConsumer implements RocketMQListener<MessageExt> {

	private final PayCoreStoreUsecase payCoreStoreUsecase;

	@Override
	public void onMessage(MessageExt msg) {
		String jsonPayload = new String(msg.getBody(), StandardCharsets.UTF_8);
		System.out.println("==========> PayCore 收到 CALLBACK_SUCCESS 事件! <==========");
		System.out.println("訊息屬性 (tag/keys): tag=" + msg.getTags() + ", keys=" + msg.getKeys()
				+ ", aggregateType=" + msg.getUserProperty("aggregateType")
				+ ", outboxId=" + msg.getUserProperty("outboxId"));
		System.out.println("事件內容 (Payload): " + jsonPayload);

		Gson gson = new Gson();
		PayCoreRechargeSuccessDto eventDto = gson.fromJson(jsonPayload, PayCoreRechargeSuccessDto.class);
		payCoreStoreUsecase.handleRechargeSuccess(eventDto);
		System.out.println("...訊息業務邏輯處理成功...");
	}
}
