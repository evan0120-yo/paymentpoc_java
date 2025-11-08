package com.citrus.payin.model;

import java.math.BigDecimal;

import com.citrus.common.model.Outbox;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payin_outbox")
public class PayinOutbox extends Outbox {
	@Id
    private String payinOutboxId; // 定義自己的主鍵
	private String userGid;
	private String orderGid;
	private String rechargeGid;
	private String refId;
	private BigDecimal actualPaymentAmount;
	private BigDecimal billAmount;
	private String rechargeInfo;

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return payinOutboxId;
	}
}
