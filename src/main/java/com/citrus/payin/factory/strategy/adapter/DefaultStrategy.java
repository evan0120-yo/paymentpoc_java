package com.citrus.payin.factory.strategy.adapter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.citrus.payin.factory.channel.PayinChannelEnum;
import com.citrus.payin.factory.strategy.PayinStrategyFactory;
import com.citrus.payin.object.dto.ExecutePaDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DefaultStrategy implements PayinStrategyFactory {
	
	@Override
	public List<String> defaultStrategy(ExecutePaDto executePaDto) {
		// TODO Auto-generated method stub
		List<String> list = new ArrayList<>();
		list.add(PayinChannelEnum.BILLDESK.getClassName());
		list.add(PayinChannelEnum.MPURSE.getClassName());
		return list;
	}
	
}