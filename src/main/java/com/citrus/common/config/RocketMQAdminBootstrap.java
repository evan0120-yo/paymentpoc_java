package com.citrus.common.config;

import java.util.Set;

import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.remoting.protocol.body.ClusterInfo;
import org.apache.rocketmq.remoting.protocol.route.BrokerData;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import com.citrus.share.enums.TopicEnum;

/**
 * Prod 環境下啟動時以 MQAdmin 依 {@link TopicEnum} 建立所有 topic。
 * 本機 dev 透過 broker 的 autoCreateTopicEnable 建立，不啟用本 bean。
 */
@Configuration
@ConditionalOnProperty(name = "rocketmq.admin.bootstrap.enabled", havingValue = "true")
public class RocketMQAdminBootstrap implements InitializingBean {

	private static final int DEFAULT_QUEUE_NUM = 8;

	@Value("${rocketmq.name-server}")
	private String nameServer;

	@Override
	public void afterPropertiesSet() throws Exception {
		DefaultMQAdminExt admin = new DefaultMQAdminExt();
		admin.setNamesrvAddr(nameServer);
		admin.setInstanceName("payment-poc-admin-bootstrap-" + System.currentTimeMillis());
		admin.start();
		try {
			ClusterInfo clusterInfo = admin.examineBrokerClusterInfo();
			Set<String> brokerNames = clusterInfo.getBrokerAddrTable().keySet();
			if (brokerNames.isEmpty()) {
				throw new IllegalStateException("RocketMQ cluster 無任何 broker，nameServer=" + nameServer);
			}
			for (String brokerName : brokerNames) {
				BrokerData brokerData = clusterInfo.getBrokerAddrTable().get(brokerName);
				String masterAddr = brokerData.selectBrokerAddr();
				for (TopicEnum topic : TopicEnum.values()) {
					TopicConfig config = new TopicConfig(topic.getTopicId());
					config.setReadQueueNums(DEFAULT_QUEUE_NUM);
					config.setWriteQueueNums(DEFAULT_QUEUE_NUM);
					admin.createAndUpdateTopicConfig(masterAddr, config);
					System.out.println("[RocketMQAdminBootstrap] topic 已建立/更新：" + topic.getTopicId()
							+ " @ " + masterAddr);
				}
			}
		} finally {
			admin.shutdown();
		}
	}
}
