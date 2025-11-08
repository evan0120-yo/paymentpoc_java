package com.citrus.payin.factory.callback.object.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CallbackPaBo {
	private MpursePaCallbackBo mpursePaCallbackBo;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class MpursePaCallbackBo {
        private String refId;
    }
}
