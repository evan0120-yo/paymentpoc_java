package com.citrus.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
		  basePackages = "com.citrus",                  // 依你的實際 package
		  transactionManagerRef = "jpaTxManager"
		)
public class TxConfig {

  @Bean("jpaTxManager")
  @Primary // 讓它成為預設的 transactionManager
  public PlatformTransactionManager jpaTxManager(EntityManagerFactory emf) {
    return new JpaTransactionManager(emf);
  }
}
