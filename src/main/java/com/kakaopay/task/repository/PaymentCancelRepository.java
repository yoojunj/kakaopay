package com.kakaopay.task.repository;

import com.kakaopay.task.entity.PaymentCancel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentCancelRepository extends JpaRepository<PaymentCancel, Long> {

    Optional<PaymentCancel> findByUuid(final String uuid);
}
