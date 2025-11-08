package com.citrus.payin.factory.strategy;

import java.util.List;

import com.citrus.payin.object.dto.ExecutePaDto;

public interface PayinStrategyFactory {
	List<String> defaultStrategy(ExecutePaDto executePaDto);
}