package com.citrus.payCore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citrus.payCore.model.BbpsRecharge;

public interface BbpsRechargeRepository extends JpaRepository<BbpsRecharge, String> {

	@Query("SELECT br "
			+ "FROM BbpsRecharge br  "
			+ "WHERE (:orderGid IS NULL OR br.orderGid = :orderGid) "
			+ "AND (:rechargeGid IS NULL OR br.rechargeGid = :rechargeGid) ")
	List<BbpsRecharge> queryBbpsRecharge(
			@Param("orderGid") String orderGid,
			@Param("rechargeGid") String rechargeGid);

	List<BbpsRecharge> findByOrderGid(String orderGid);
}
