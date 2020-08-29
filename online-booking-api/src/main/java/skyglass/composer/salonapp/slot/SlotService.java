package skyglass.composer.salonapp.slot;


import lombok.extern.slf4j.Slf4j;
import skyglass.composer.salonapp.salonservice.SalonService;
import skyglass.composer.salonapp.salonservice.SalonServiceDetail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SlotService {


    SlotRepository slotRepository;


    SalonService salonService;




    public  SlotService(SalonService salonService, SlotRepository slotRepository){
        this.salonService=salonService;
        this.slotRepository=slotRepository;
    }


    public Optional<Slot> findAvailableSlotId(Long slotId) {

        return slotRepository.findByIdAndStatus(slotId,SlotStatus.AVAILABLE);

    }

    public Optional<Slot> findLockedSlotId(Long slotId) {

        return slotRepository.findByIdAndStatus(slotId,SlotStatus.LOCKED);

    }


    public List<Slot> getSlotsForServiceOnDate(Long slotServiceId, String formattedDate) {

        SalonServiceDetail salonServiceDetail =  salonService.findById(slotServiceId).orElseThrow(()->new RuntimeException("Invalid Service"));



        LocalDate localDate = getAsDate(formattedDate);

        LocalDateTime startDate = localDate.atTime(0,1);
        LocalDateTime endDate = localDate.atTime(23,59);
        log.info("Querying for  " + slotServiceId + " From " + startDate + " to " +  endDate);
        List<Slot> results = slotRepository.findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatus(startDate, endDate, salonServiceDetail,SlotStatus.AVAILABLE);

        log.info("returning  " + results.size() + " Slots");
        return results;
    }

    public LocalDate getAsDate(String formattedDate){

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return LocalDate.parse(formattedDate, formatter);

    }


    public void setToConfirmed(Slot slot) {
        slot.setStatus(SlotStatus.CONFIRMED);
        slot.setConfirmedAt(LocalDateTime.now());
        save(slot);
    }
    public void setToAvailable(Slot slot) {
        slot.setStatus(SlotStatus.AVAILABLE);
        slot.setSelectedService(null);
        slot.setLockedAt(null);
        save(slot);
    }

    public void setToLockedWithService(Slot slot, SalonServiceDetail serviceDetail) {
        slot.setStatus(SlotStatus.LOCKED);
        slot.setLockedAt(LocalDateTime.now());
        slot.setSelectedService(serviceDetail);
        save(slot);
    }
    @Transactional
    public void save(Slot slot) {
        slotRepository.save(slot);
    }


}

