package com.citrus.payCore.model;

import com.citrus.common.task.PollingTask;
import com.citrus.payCore.enums.RechargeRetryStatusEnum;
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
@Table(name="recharge_retry")
public class RechargeRetry implements PollingTask {
	@Id
    private String rechargeRetryId;
    private String refId;
    @Enumerated(EnumType.STRING)
    private RechargeRetryStatusEnum status;
    private String instanceId;
    private int retryCount;
    private Timestamp createTime;
    private Timestamp updateTime;
	
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return this.rechargeRetryId;
	}

	@Override
	public String getTaskType() {
		// TODO Auto-generated method stub
		return "RECHARGE_RETRY";
	}

}
