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
@Table(name="biller_gateway_support")
public class BillerGatewaySupport {
    @Id
    private String billerId; // 外鍵，指向 BbpsBiller

    @Id
    private String gatewayId; // 外鍵，指向 PaymentGateway

    /**
     * 這個 Biller 在該 Gateway 系統中的代碼。
     * 有時候，同一個 Airtel，在 Mpurse 和 Billdesk 的代碼可能不同。
     */
    private String billerCodeOnGateway;
    
    private boolean isEnabled; // 是否啟用這條支援路徑
}
