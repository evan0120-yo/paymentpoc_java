package com.citrus.biller.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
@Table(name="user_pending_bill")
public class UserPendingBill {
	@Id
    private String id; // 主鍵

    private String userId; // 關聯到哪個用戶

    private String billerId; // 關聯到哪個 Biller
    private String billerName; // 反正規化，直接儲存 Biller 名稱
    private String billerLogoUrl; // 反正規化，直接儲存 Logo

    private String billId; // 這張帳單在 Biller 系統中的唯一 ID
    private String billDescription; // 帳單描述，e.g., "2025年9月電信費"
    private BigDecimal amountDue; // 待繳金額
    private LocalDate dueDate; // 到期日
    
    private LocalDateTime lastRefreshedAt; // 最後一次與 Biller 系統同步的時間
}
