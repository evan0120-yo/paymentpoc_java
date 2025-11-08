package com.citrus.payin.factory.channel.adapter;

import org.springframework.stereotype.Service;

import com.citrus.payin.factory.channel.PayinChannelFactory;
import com.citrus.payin.factory.channel.object.bo.PaPaymentBo;
import com.citrus.payin.factory.channel.object.bo.PgPaymentBo;
import com.citrus.payin.factory.channel.object.bo.QueryBillStatusBo;
import com.citrus.payin.factory.channel.object.bo.PaPaymentBo.MpursePaPaymentBo;
import com.citrus.payin.factory.channel.object.dto.PaPaymentDto;
import com.citrus.payin.factory.channel.object.dto.PgPaymentDto;
import com.citrus.payin.factory.channel.object.dto.QueryBillStatusDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BilldeskChannel implements PayinChannelFactory {
	
	@Override
	public PaPaymentDto initiatePaPayment(PaPaymentBo paPaymentBo) {
		// TODO Auto-generated method stub
		MpursePaPaymentBo bo = paPaymentBo.getMpursePaPaymentBo();
		String payload =
			    "{"
			  + "\"product_code\":\""      + bo.getProductCode()      + "\","
			  + "\"merchant_order_no\":\"" + bo.getMerchantOrderNo()  + "\","
			  + "\"currency\":\""          + bo.getCurrency()         + "\","
			  + "\"amount\":\""            + bo.getAmount()           + "\","
			  + "\"notify_url\":\""        + bo.getNotifyUrl()        + "\","
			  + "\"callback_url\":\""      + bo.getCallbackUrl()      + "\","
			  + "\"customerid\":\""        + bo.getCustomerid()       + "\","
			  + "\"remark\":\""            + bo.getRemark()           + "\""
			  + "}";
		return PaPaymentDto.builder()
				.channelName("Billdesk")
				.channelTxnId("testBilldeskFinalChannelTxnId")
				.amount(bo.getAmount())
				.currency(bo.getCurrency())
				.payloadResp(payload)
				.isSuccess(false)
				.build();
	}

	@Override
	public PgPaymentDto initiatePgPayment(PgPaymentBo pgPaymentBo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public QueryBillStatusDto queryBillStatus(QueryBillStatusBo queryBillStatusBo) {
		// TODO Auto-generated method stub
		return null;
	}

}
