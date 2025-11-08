package com.citrus.payin.factory.channel.object.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PgPaymentBo {

    private MpursePgPaymentBo mpursePgPaymentBo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MpursePgPaymentBo {
        private String productCode;
    }
}