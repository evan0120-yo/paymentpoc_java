package com.citrus.payCore.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.common.enums.TaskStatus;
import com.citrus.common.repository.OutboxRepository;
import com.citrus.payCore.model.PayCoreOutbox;

public interface PayCoreOutboxRepository extends OutboxRepository<PayCoreOutbox, String> {

	 /**
     * 繼承了通用的 findByInstanceIdAndOutboxStatus, updateStatusByInstanceId 等方法。
     * 這裡我們定義針對 payin_outbox 表的專屬原生 SQL 查詢。
     */
	@Override
    @Modifying
    @Query(value = "UPDATE pay_core_outbox SET status = 'PROCESSING', instance_id = :instanceId " + // <-- 改成 status
            "WHERE pay_core_outbox_id IN ( " +
            "    SELECT pay_core_outbox_id FROM pay_core_outbox " +
            "    WHERE status = 'PENDING' ORDER BY created_time ASC LIMIT :limit FOR UPDATE SKIP LOCKED" + // <-- 改成 status
            ")", nativeQuery = true)
	int claimPendingTasks(@Param("instanceId") String instanceId, @Param("limit") int limit);
	
	@Override
    default TaskStatus getProcessingStatus() {
        return OutboxStatusEnum.PROCESSING; // 使用它自己的 Enum
    }
	
	@Override
	default TaskStatus getSuccessStatus() {
		return OutboxStatusEnum.SUCCESS; // 使用它自己的 Enum
	}
}
