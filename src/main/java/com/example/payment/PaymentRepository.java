package com.example.payment;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;


public interface PaymentRepository extends CrudRepository<Payment, Long> {

	Optional<Payment> findByPurchaseId(Long purchaseId);
}
