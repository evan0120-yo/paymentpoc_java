package com.citrus.common.model;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.common.task.PollingTask;
import com.google.cloud.Timestamp;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
@SuperBuilder
public abstract class Outbox implements PollingTask {
	
	public abstract String getId();
    @Enumerated(EnumType.STRING)
    private OutboxStatusEnum status;
    private String topicId;
    private String aggregateId;					// 對應的物件id
    private String aggregateType;
    private String eventType;					// 實作的outbox自行定義enum處理
    private String instanceId;					// 紀錄哪個uuid認領的
    private String payload;						// 類似數據驅動
    private Timestamp createdTime;
    private Timestamp publishedTime;
    
    @Override
    public String getTaskType() {
        // Outbox 的任務類型，就是它的 EventType
        return this.getEventType();
    }
}
