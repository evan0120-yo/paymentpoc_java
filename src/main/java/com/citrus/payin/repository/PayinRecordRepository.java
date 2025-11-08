package com.citrus.payin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citrus.payin.model.PayinRecord;

public interface PayinRecordRepository extends JpaRepository<PayinRecord,String>{

	List<PayinRecord> findByRefId(String refId);
}
