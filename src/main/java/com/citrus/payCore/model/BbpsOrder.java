package com.citrus.payCore.model;

import java.math.BigDecimal;

import org.springframework.data.domain.Persistable;

import com.citrus.payCore.enums.BbpsOrderStatusEnum;
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
@Table(name="bbps_order")
public class BbpsOrder implements Persistable<String> {
	@Id
	@Column(name = "order_gid")
	private String orderGid;
	private String userGid;
	@Column(name = "ref_id", nullable = false, unique = true)
	private String refId;			// 每個帳單第三方給的唯一id
	private BigDecimal billAmount;
	private BigDecimal actualPaymentAmount;
    @Enumerated(EnumType.STRING)
	private BbpsOrderStatusEnum bbpsOrderStatus;
	private Timestamp createTime;
	private Timestamp updateTime;
    @Version
    @Column(name = "version")
    private Long version;
    
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return orderGid;
	}
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return version == null;
	}
}
