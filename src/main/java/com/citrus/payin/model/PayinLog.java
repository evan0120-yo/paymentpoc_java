package com.citrus.payin.model;

import com.citrus.payin.enums.PayinActionEnum;
import com.citrus.payin.enums.PayinDirectionEnum;
import com.google.cloud.Timestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(name = "payin_log")
public class PayinLog {
    @Id
    private String payLogId; // 這條日誌的唯一 ID
    private String payinRecordId;
    private String attemptId;
    private String orderGid;
    private String rechargeGid;
    private String refId;
    @Enumerated(EnumType.STRING)
    private PayinActionEnum payinAction;
    @Enumerated(EnumType.STRING)
    private PayinDirectionEnum payinDirection;
    private String payload;
    private String header;
    private Timestamp createTime;
}
