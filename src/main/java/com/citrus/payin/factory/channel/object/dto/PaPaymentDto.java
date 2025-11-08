package com.citrus.payin.factory.channel.object.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaPaymentDto {
	private Boolean isSuccess;	// call甲方有無成功
	private String channelName;
    private String channelTxnId;
    private BigDecimal amount;
    private String currency;
    private String headerReq;
    private String payloadReq;
    private String headerResp;
    private String payloadResp;
}
