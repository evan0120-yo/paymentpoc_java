package com.citrus.payin.object.bo;

import com.citrus.payin.model.PayinAttempt;
import com.citrus.payin.model.PayinLog;
import com.citrus.payin.model.PayinRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayinBo {
	private PayinRecord payinRecord;
	private PayinAttempt payinAttempt;
	private PayinLog payinLog;
	
	public boolean hasOrder() {
		return payinRecord != null;
	}
}
