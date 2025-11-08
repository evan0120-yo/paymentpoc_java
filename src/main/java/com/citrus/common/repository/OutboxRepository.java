package com.citrus.common.repository;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.citrus.common.enums.OutboxStatusEnum;
import com.citrus.common.enums.TaskStatus;
import com.citrus.common.model.Outbox;

@NoRepositoryBean
public interface OutboxRepository<T extends Outbox, ID> extends PollingTaskRepository<T, ID> {

	 /**
     * findBy... 方法 Spring Data JPA 可以自動實現，不需要 @Query，
     * 但為了清晰，我們覆寫它，確保參數型別正確。
     */
    List<T> findByInstanceIdAndStatus(String instanceId, TaskStatus status);
    
    @Override
    default TaskStatus getProcessingStatus() {
        return OutboxStatusEnum.PROCESSING;
    }
}
