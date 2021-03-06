package skyglass.composer.salonapp.slot;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import skyglass.composer.salonapp.salonservice.SalonServiceDetail;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface SlotRepository extends JpaRepository<Slot,Long> {

    List<Slot> findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatus(LocalDateTime startTime, LocalDateTime endTime, SalonServiceDetail serviceDetail,SlotStatus status);
    Optional<Slot> findByIdAndStatus(Long id,SlotStatus slotStatus);

}
