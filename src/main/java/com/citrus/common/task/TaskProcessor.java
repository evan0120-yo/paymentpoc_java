package com.citrus.common.task;

import java.util.List;

public interface TaskProcessor<T extends PollingTask> {
	void process(T tasks);
	List<String> getSupportedTypes(); 
}
