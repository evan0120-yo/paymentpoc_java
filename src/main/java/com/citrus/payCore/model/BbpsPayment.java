package com.citrus.payCore.model;

import java.math.BigDecimal;

import org.springframework.data.domain.Persistable;

import com.citrus.payCore.enums.BbpsPaymentStatusEnum;
import com.google.cloud.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name="bbps_payment")
public class BbpsPayment implements Persistable<String>{
	@Id
	@Column(name = "payment_gid")
	private String paymentGid;
	private String orderGid;
	private BigDecimal actualPaymentAmount;
    @Enumerated(EnumType.STRING)
	private BbpsPaymentStatusEnum bbpsPaymentStatus;
	private Timestamp createTime;
	private Timestamp updateTime;
    @Version
    @Column(name = "version")
    private Long version;
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return paymentGid;
	}
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return version == null;
	}
}
