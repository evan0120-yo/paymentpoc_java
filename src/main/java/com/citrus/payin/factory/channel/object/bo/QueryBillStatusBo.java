package com.citrus.payin.factory.channel.object.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QueryBillStatusBo {

    private MpurseQueryBillStatusBo mpurseQueryBillStatusBo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MpurseQueryBillStatusBo {
        private String productCode;
    }
}
