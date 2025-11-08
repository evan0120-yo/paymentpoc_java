package com.citrus.payCore.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citrus.common.enums.TaskStatus;
import com.citrus.common.repository.PollingTaskRepository;
import com.citrus.payCore.enums.RechargeRetryStatusEnum;
import com.citrus.payCore.model.RechargeRetry;

public interface RechargeRetryRepository extends PollingTaskRepository<RechargeRetry, String> {

	/**
     * 2. 為 claimPendingTasks 提供針對 recharge_retry 表的具體 SQL 實作。
     * 這段 SQL 和你 Outbox 裡的那段幾乎一模一樣，只是把操作的表名和主鍵名換掉了。
     */
    @Override
    @Modifying
    @Query(value = "UPDATE recharge_retry SET status = 'PROCESSING', instance_id = :instanceId " +
            "WHERE recharge_retry_id IN ( " +
            "    SELECT recharge_retry_id FROM recharge_retry " +
            "    WHERE status = 'PENDING' ORDER BY create_time ASC LIMIT :limit FOR UPDATE SKIP LOCKED" +
            ")", nativeQuery = true)
    int claimPendingTasks(@Param("instanceId") String instanceId, @Param("limit") int limit);

    @Override
    default TaskStatus getProcessingStatus() {
        return RechargeRetryStatusEnum.PROCESSING; // 使用它自己的 Enum
    }
    
    @Override
    default TaskStatus getSuccessStatus() {
        return RechargeRetryStatusEnum.SUCCESS; // 使用它自己的 Enum
    }
    
    Optional<RechargeRetry> findByRefId(String refId);
}
