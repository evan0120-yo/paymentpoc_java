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
@Table(name="bbps_biller")
public class BbpsBiller {

    /**
     * Biller 在 BBPS 系統中的唯一識別碼，或我方系統的內部ID。
     */
    @Id
    private String billerId;

    /**
     * Biller 的官方名稱，用於 UI 顯示。
     * e.g., "Airtel", "Vodafone Idea"
     */
    private String name;

    /**
     * Biller 的類別，用於分類和搜尋。
     * e.g., "Telecom", "Electricity"
     */
    private String category;
    
    /**
     * Biller 的 Logo 圖片 URL，用於 UI 顯示。
     */
    private String logoUrl;

    /**
     * Biller 在我方系統的狀態 (是否啟用)。
     * e.g., "ACTIVE", "INACTIVE"
     */
    private String status;
}
