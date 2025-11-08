package com.citrus.payCore.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.citrus.payCore.model.BbpsOrder;

public interface BbpsOrderRepository extends JpaRepository<BbpsOrder,String>{

	@Query("SELECT bo "
	           + "FROM BbpsOrder bo  " 
	           + "WHERE (:orderGid IS NULL OR bo.orderGid = :orderGid) "
	           + "AND (:userGid IS NULL OR bo.userGid = :userGid) "
	           )
	List<BbpsOrder> queryBbpsOrder(
	        @Param("orderGid") String orderGid,
	        @Param("userGid") String userGid
			);
	
	List<BbpsOrder> findByRefId(String refId);
	
	Optional<BbpsOrder> findByOrderGid(String orderGid);
}
