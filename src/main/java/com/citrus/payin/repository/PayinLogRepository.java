package com.citrus.payin.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citrus.payin.model.PayinLog;

public interface PayinLogRepository extends JpaRepository<PayinLog,String> {

}
