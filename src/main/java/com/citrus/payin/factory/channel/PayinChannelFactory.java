package com.citrus.payin.factory.channel;

import com.citrus.payin.factory.channel.object.bo.PaPaymentBo;
import com.citrus.payin.factory.channel.object.bo.PgPaymentBo;
import com.citrus.payin.factory.channel.object.bo.QueryBillStatusBo;
import com.citrus.payin.factory.channel.object.dto.PaPaymentDto;
import com.citrus.payin.factory.channel.object.dto.PgPaymentDto;
import com.citrus.payin.factory.channel.object.dto.QueryBillStatusDto;

public interface PayinChannelFactory {
	PaPaymentDto initiatePaPayment(PaPaymentBo paPaymentBo);
	PgPaymentDto initiatePgPayment(PgPaymentBo pgPaymentBo);
	QueryBillStatusDto queryBillStatus(QueryBillStatusBo queryBillStatusBo);
}
