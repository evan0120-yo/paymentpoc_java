package com.citrus.payin.factory.channel.object.dto;

import java.math.BigDecimal;

import com.citrus.payin.model.PayinRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SavePaPayinDto {
	private String orderGid;
	private String rechargeGid;
	private String userGid;
	private String billGid;
	private String refId;
	private BigDecimal actualPaymentAmount;
	private String rechargeInfo;
	
	private PaPaymentDto paPaymentDto;
	private PayinRecord payinRecord;
}
