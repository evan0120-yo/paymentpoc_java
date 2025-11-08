package com.citrus.payin.factory.channel.object.bo;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaPaymentBo {
	
    private MpursePaPaymentBo mpursePaPaymentBo;
    private BilldeskPaPaymentBo billdeskPaPaymentBo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MpursePaPaymentBo {
        private String productCode;
        private String merchantOrderNo;
        private String currency;
        private BigDecimal amount;
        private String notifyUrl;
        private String callbackUrl;
        private String customerid;
        private String remark;
    }
    
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BilldeskPaPaymentBo {
        private String productCode;
        private String merchantOrderNo;
        private String currency;
        private BigDecimal amount;
        private String notifyUrl;
        private String callbackUrl;
        private String customerid;
        private String remark;
    }
}
