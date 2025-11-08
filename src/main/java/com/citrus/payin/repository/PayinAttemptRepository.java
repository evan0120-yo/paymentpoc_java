package com.citrus.payin.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.citrus.payin.enums.PayinAttemptStatusEnum;
import com.citrus.payin.model.PayinAttempt;

public interface PayinAttemptRepository extends JpaRepository<PayinAttempt,String> {

	List<PayinAttempt> findByRefIdAndPayinAttemptStatus(String refId, PayinAttemptStatusEnum status);
}
