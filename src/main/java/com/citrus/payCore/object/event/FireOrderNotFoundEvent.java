package com.citrus.payCore.object.event;

import com.citrus.common.object.BasicEvent;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class FireOrderNotFoundEvent extends BasicEvent {
	private String refId;
}