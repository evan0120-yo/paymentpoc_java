package com.citrus.payCore.model;

import org.springframework.data.domain.Persistable;

import com.citrus.payCore.enums.BbpsRechargeStatusEnum;
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
@Table(name="bbps_recharge")
public class BbpsRecharge implements Persistable<String>{
	@Id
	@Column(name = "recharge_gid")
	private String rechargeGid;
	private String orderGid;
	private String rechargeInfo;		// JSON  裡面包含VPA
    @Enumerated(EnumType.STRING)
	private BbpsRechargeStatusEnum bbpsRechargeStatus;
	private Timestamp createTime;
	private Timestamp updateTime;
    @Version
    @Column(name = "version")
    private Long version;
	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return rechargeGid;
	}
	@Override
	public boolean isNew() {
		// TODO Auto-generated method stub
		return version == null;
	}
}
