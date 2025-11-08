package com.citrus.common.publisher;

import com.citrus.common.model.Outbox;
import com.citrus.common.task.TaskProcessor;

public interface EventPublisher extends TaskProcessor<Outbox> {
	
}
