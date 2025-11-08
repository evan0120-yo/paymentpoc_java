package com.citrus.payLedger.consumer;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.citrus.share.enums.PubSubEnum;
import com.google.api.core.ApiService.Listener;
import com.google.api.core.ApiService.State;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.BasicAcknowledgeablePubsubMessage;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.pubsub.v1.PubsubMessage;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LedgerEventConsumer {

	private final PubSubTemplate pubSubTemplate;

	@PostConstruct
	public void startSubscription() {
		String subscriptionId = PubSubEnum.RECHARGE_SUCCESS.getSubscriptionMap().get("PAYLEDGER");
		System.out.println("PayLedger 消費者啟動 (程式化訂閱模式)，開始監聽 Subscription: " + subscriptionId);
	        
		Subscriber sub = pubSubTemplate.subscribe(subscriptionId, (BasicAcknowledgeablePubsubMessage msg) -> {
			PubsubMessage pubsubMessage = msg.getPubsubMessage();
		    String jsonPayload = pubsubMessage.getData().toStringUtf8();
		    Map<String, String> attributes = pubsubMessage.getAttributesMap();
		    
		    System.out.println("==========> 💰 PayLedger 收到新事件! <==========");
		    System.out.println("訊息屬性 (Attributes): " + attributes);
		    System.out.println("事件內容 (Payload): " + jsonPayload);
		            
		    try {
		    	// --- 這裡是未來處理業務邏輯的地方 ---
		        // TODO: 驗證 attributes、反序列化 payload、呼叫 use case 等
		                
		        // 模擬處理成功
		        System.out.println("...訊息業務邏輯處理成功...");
	
		        // 業務邏輯成功後，發送 ACK
		        System.out.println("發送 ACK 確認消息。");
		        msg.ack();
	
		    } catch (Exception e) {
		    	// 如果在 try 的過程中發生任何錯誤
		        System.err.println("訊息處理發生錯誤，發送 NACK。錯誤: " + e.getMessage());
		                
		        // 發送 NACK，請求 Pub/Sub 重新投遞或轉發到死信隊列
		        msg.nack();
		    }
		});
		
		// ====== 只加監聽 & sysout（不改其他行為） ======
	    sub.addListener(new Listener() {
	        @Override public void running() {
	            System.out.println("[" + subscriptionId + "] subscriber RUNNING");
	        }
	        @Override public void failed(State from, Throwable failure) {
	            System.err.println("[" + subscriptionId + "] subscriber FAILED from " + from + ": " + failure);
	        }
	        @Override public void terminated(State from) {
	            System.out.println("[" + subscriptionId + "] subscriber TERMINATED from " + from);
	        }
	    }, MoreExecutors.directExecutor());
	}
}
