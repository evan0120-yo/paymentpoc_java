package com.citrus.common.schedule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.citrus.common.enums.TaskStatus;
import com.citrus.common.repository.PollingTaskRepository;
import com.citrus.common.task.PollingTask;
import com.citrus.common.task.TaskProcessor;
import com.fasterxml.uuid.Generators;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PollingTaskScheduler {

    private final List<PollingTaskRepository<? extends PollingTask, ?>> taskRepositories;

    private final Map<String, TaskProcessor> processorMap;

    @Autowired
    public PollingTaskScheduler(
            List<PollingTaskRepository<? extends PollingTask, ?>> taskRepositories,
            List<TaskProcessor<?>> processors) { // <-- 4. 這裡注入的是所有 TaskProcessor

        this.taskRepositories = taskRepositories;
        this.processorMap = new HashMap<>();
        for (TaskProcessor<?> processor : processors) {
            for (String taskType : processor.getSupportedTypes()) {
                // 如果有重複的 key，後來的會覆蓋前面的，這裡加上一個警告提示
                if (this.processorMap.containsKey(taskType)) {
                    System.err.println(
                            "WARNING: Duplicate TaskProcessor mapping for type '" + taskType + "'. Overwriting.");
                }
                this.processorMap.put(taskType, processor);
            }
        }

        System.out.println("Initialized PollingTaskScheduler with " + this.taskRepositories.size() +
                " repositories and " + this.processorMap.size() + " task type mappings.");
    }

    @Scheduled(cron = "0/10 * * * * ?")
    @Transactional
    public void processOutboxEvents() {
        String instanceId = Generators.timeBasedEpochGenerator().generate().toString();
        for (PollingTaskRepository repo : taskRepositories) {
            // OutboxStatusEnum.PROCESSING 也需要換成通用的 TaskStatus
            // 這裡我們先假設你的 OutboxStatusEnum 和 PendingCallbackStatus 都有 PROCESSING
            TaskStatus processingStatus = repo.getProcessingStatus();
            // 認領outbox
            int claimedCount = repo.claimPendingTasks(instanceId, 100);

            if (claimedCount > 0) {
                // 找到自己認領的outbox
                List<? extends PollingTask> tasksToProcess = repo.findByInstanceIdAndStatus(instanceId,
                        processingStatus);

                for (PollingTask task : tasksToProcess) {
                    TaskProcessor processor = processorMap.get(task.getTaskType());

                    if (processor != null) {
                        processor.process(task);
                    } else {
                        System.err.println("No processor found for task type: " + task.getTaskType());
                    }
                }
                // 這邊的狀態更新也需要使用 TaskStatus
                repo.updateStatusByInstanceId(repo.getSuccessStatus(), instanceId);
            }
        }
    }
}
