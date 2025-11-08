package com.citrus.payCore.object.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbpsRechargeQueryBo {
	private String rechargeGid;
	private String orderGid;
	private String billGid;
}
