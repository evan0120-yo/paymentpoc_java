package com.citrus.payCore.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citrus.payCore.model.BbpsPayment;

public interface BbpsPaymentRepository extends JpaRepository<BbpsPayment,String>{

	@Query("SELECT bp "
	           + "FROM BbpsPayment bp  " 
	           + "WHERE (:orderGid IS NULL OR bp.orderGid = :orderGid) "
	           + "AND (:paymentGid IS NULL OR bp.paymentGid = :paymentGid) "
	           )
	List<BbpsPayment> queryBbpsPayment(
	        @Param("orderGid") String orderGid,
	        @Param("paymentGid") String paymentGid
			);
	
	List<BbpsPayment> findByOrderGid(String orderGid);
}

