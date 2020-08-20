package com.manning.salonapp.payment;

import com.manning.salonapp.payment.models.Payment;
import com.manning.salonapp.slot.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findBySlotAndEmail(Slot slot, String email);

}
