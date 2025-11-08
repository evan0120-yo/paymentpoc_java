package com.citrus.payin.model;

import java.math.BigDecimal;

import org.springframework.data.domain.Persistable;

import com.citrus.payin.enums.PayinStatusEnum;
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
@Table(name = "payin_record")
public class PayinRecord implements Persistable<String>{
    @Id
    private String payinRecordId;
    private String orderGid;
    private String rechargeGid;
    private String refId;
    private String finalChannelName;
    private String finalChannelTxnId;
    private BigDecimal amount;
    private String currency;
    /**
     * 【交易狀態】
     * 欄位理由：記錄這筆「外部交易」本身的最終狀態。它與我們 payCore 的內部狀態是分離的，
     * 專門反映與外部渠道溝通的結果。
     * 範例: "PENDING", "SUCCESS", "FAILED"
     */
    @Enumerated(EnumType.STRING)
    private PayinStatusEnum payinStatus;
    private Timestamp createdTime;
    private Timestamp updateTime;
    private Timestamp completedTime;
    
    @Version
    @Column(name = "version")
    private Long version;
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return payinRecordId;
	}
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return version == null;
	}
}
