package com.citrus.biller.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="payment_gateway")
public class PaymentGateway {
	 @Id
	 private String gatewayId; // e.g., "MPURSE", "BILLDESK"
	 private String gatewayName; // e.g., "Mpurse Payment Gateway"
	 private boolean isEnabled;
	 // ... 其他渠道級別的配置，例如 API Endpoint, Key/Secret 的儲存位置等
}
