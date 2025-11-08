package com.citrus.payin.model;

import java.math.BigDecimal;

import org.springframework.data.domain.Persistable;

import com.citrus.payin.enums.PayinAttemptStatusEnum;
import com.google.cloud.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "payin_attempt")
public class PayinAttempt implements Persistable<String>{

    @Id
    private String payinAttemptId;
    private String payinRecordId;
    private String orderGid;
    private String rechargeGid;
    private String refId;
    private String channelName;
    private String channelTxId;
    private BigDecimal amount;
    private String currency;
    /**
     * 【請求黑盒子 / 鐵證】
     * 欄位理由：儲存這「一次嘗試」的、發送給渠道方的完整請求 JSON。
     */
    private String channelReqPayload;
    private String headersReq;

    /**
     * 【回應黑盒子 / 鐵證】
     * 欄位理由：儲存這「一次嘗試」的、從渠道方收到的、決定了本次嘗試狀態的最終回應 JSON。
     */
    private String channelRespPayload;
    
    @Enumerated(EnumType.STRING)
    private PayinAttemptStatusEnum payinAttemptStatus;
    private Timestamp createdTime;
    private Timestamp updateTime;
    private Timestamp completedTime;
    
    @Version
    @Column(name = "version")
    private Long version;
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return payinAttemptId;
	}
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return version == null;
	}
}