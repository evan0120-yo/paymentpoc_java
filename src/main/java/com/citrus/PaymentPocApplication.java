package com.citrus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class PaymentPocApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentPocApplication.class, args);
		System.out.println("=============== START ===============");
	}
}