package com.citrus.payCore.object.bo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BbpsOrderQueryBo {
	private String orderGid;
	private String userGid;
	private String billGid;
}
