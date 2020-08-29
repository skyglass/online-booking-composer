package skyglass.salonapp.payment;

import skyglass.salonapp.payment.models.Payment;
import skyglass.salonapp.slot.Slot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment,Long> {

    Payment findBySlotAndEmail(Slot slot, String email);

}
