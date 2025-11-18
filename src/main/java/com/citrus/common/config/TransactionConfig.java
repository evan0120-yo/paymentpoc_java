package com.citrus.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

@Configuration
public class TransactionConfig {
    
    // 1. 基本款（預設設定）
    @Bean
    public TransactionTemplate transactionTemplate(
            PlatformTransactionManager txManager) {
        return new TransactionTemplate(txManager);
        // 預設：
        // - Isolation: READ_COMMITTED
        // - Timeout: -1（無限制）
        // - Propagation: REQUIRED
    }
    
    // 2. 一般場景（讀已提交，30 秒超時）
    @Bean
    public TransactionTemplate readCommittedTemplate(
            PlatformTransactionManager txManager) {
        TransactionTemplate template = new TransactionTemplate(txManager);
        template.setIsolationLevel(
            TransactionDefinition.ISOLATION_READ_COMMITTED);
        template.setTimeout(30);
        template.setPropagationBehavior(
            TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }
    
    // 3. 金額場景（序列化，10 秒超時，永遠新建）
    @Bean
    public TransactionTemplate serializableTemplate(
            PlatformTransactionManager txManager) {
        TransactionTemplate template = new TransactionTemplate(txManager);
        template.setIsolationLevel(
            TransactionDefinition.ISOLATION_SERIALIZABLE);
        template.setTimeout(10);
        template.setPropagationBehavior(
            TransactionDefinition.PROPAGATION_REQUIRES_NEW);  // 永遠新建
        return template;
    }
    
    // 4. 日誌場景（讀未提交，快速，獨立事務）
    @Bean
    public TransactionTemplate logTemplate(
            PlatformTransactionManager txManager) {
        TransactionTemplate template = new TransactionTemplate(txManager);
        template.setIsolationLevel(
            TransactionDefinition.ISOLATION_READ_UNCOMMITTED);
        template.setTimeout(5);
        template.setPropagationBehavior(
            TransactionDefinition.PROPAGATION_REQUIRES_NEW);  // 獨立事務
        return template;
    }
}