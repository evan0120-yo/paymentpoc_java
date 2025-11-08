package com.citrus.common.config;

import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import com.citrus.share.enums.PubSubEnum;
import com.google.cloud.spring.pubsub.PubSubAdmin;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class PubSubConfig implements InitializingBean {


    private final PubSubAdmin pubSubAdmin;

    /**
     * 這個方法會在 Spring Bean 初始化完成後自動執行
     */
    @Override
    public void afterPropertiesSet() {
    	for(PubSubEnum channel : PubSubEnum.values()){
    	    String topicId = channel.getTopicId();
    	    Map<String,String> subscriptionIdMap = channel.getSubscriptionMap();
        	// --- 檢查並建立 Topic ---
    	    if(pubSubAdmin.getTopic(topicId) == null) {
    	    	pubSubAdmin.createTopic(topicId);
    	    }
    	    if(!subscriptionIdMap.isEmpty()) {
    	    	for(String subscriptionId : subscriptionIdMap.values()) {
    	    	    if(pubSubAdmin.getSubscription(subscriptionId) == null) {
    	    	    	pubSubAdmin.createSubscription(subscriptionId, topicId);
    	    	    }
    	    	}
    	    }
    	}
    }
}