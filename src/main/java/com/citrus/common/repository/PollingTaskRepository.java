package com.citrus.common.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

import com.citrus.common.enums.TaskStatus;
import com.citrus.common.task.PollingTask;

@NoRepositoryBean
public interface PollingTaskRepository<T extends PollingTask, ID> extends JpaRepository<T, ID> {

	/**
     * 認領待處理的任務。
     * 具體的 SQL 查詢由各個子介面使用 @Query 來實現。
     */
    int claimPendingTasks(@Param("instanceId") String instanceId, @Param("limit") int limit);

    /**
     * 根據 instanceId 和狀態，查詢被當前實例認領的事件。
     * 注意：狀態參數使用 Enum<?> 比較靈活，但更好的做法是讓狀態 Enum 也繼承一個通用介面。
     * 這裡我們先用 OutboxStatusEnum，因為它們的狀態是一致的。
     */
    List<T> findByInstanceIdAndStatus(String instanceId, TaskStatus status);
    
    /**
     * 根據 instanceId 更新事件狀態。
     */
    @Modifying
    @Query("UPDATE #{#entityName} o SET o.status = :status " +
           "WHERE o.instanceId = :instanceId AND o.status = 'PROCESSING'")
    int updateStatusByInstanceId(TaskStatus status, String instanceId);
    
    /**
     * 讓 Repository 自己提供「處理中」對應的狀態枚舉。
     * 這樣排程器就不需要知道具體的 Enum 類別。
     */
    TaskStatus getProcessingStatus();
    TaskStatus getSuccessStatus();
}
