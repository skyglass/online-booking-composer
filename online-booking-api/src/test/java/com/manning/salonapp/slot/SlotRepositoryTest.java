package com.manning.salonapp.slot;

import com.manning.salonapp.mocks.TestDbInitializationService;

import lombok.extern.slf4j.Slf4j;
import skyglass.salonapp.salonservice.SalonServiceDetail;
import skyglass.salonapp.salonservice.SalonServiceDetailRepository;
import skyglass.salonapp.slot.Slot;
import skyglass.salonapp.slot.SlotRepository;
import skyglass.salonapp.slot.SlotStatus;

import org.hamcrest.core.IsNot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsEqual.equalTo;

@DataJpaTest
@Slf4j
class SlotRepositoryTest {

    @Autowired
    SlotRepository slotRepository;

    @Autowired
    SalonServiceDetailRepository salonServiceDetailRepository;


    @BeforeEach
    public void before() {
        TestDbInitializationService testDB = new TestDbInitializationService(salonServiceDetailRepository, slotRepository);
        testDB.initDb();
    }

    @Test
    void countShouldBeGreaterThanZero() {

        List<Slot> slots = slotRepository.findAll();
        assertThat(slots.size(), greaterThan(0));

    }

    @Test
    void findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatusShouldGiveExactValueForAvailableSlots() {

        LocalDate localDate = LocalDate.now().plusDays(2);
        List<Slot> results = getSlotsOnDayForService(getASalonServiceDetail(), localDate);
        assertResults(localDate, results);


    }

    private void assertResults(LocalDate localDate, List<Slot> results) {
        Long validCount = getValidCountFromResults(localDate, results);
        assertThat(results.size(), equalTo(validCount.intValue()));
    }

    private Long getValidCountFromResults(LocalDate localDate, List<Slot> results) {
        return results.stream()
                .filter(slot -> slot.getStatus().equals(SlotStatus.AVAILABLE))
                .filter(slot -> slot.getSlotFor().getDayOfMonth() == localDate.getDayOfMonth())
                .count();
    }

    private SalonServiceDetail getASalonServiceDetail() {
        List<SalonServiceDetail> services = salonServiceDetailRepository.findAll();
        return services.get(0);
    }

    @Test
    void findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatusShouldGiveExactValueForSomeAvailableSlots() {

        SalonServiceDetail salonServiceDetail = getASalonServiceDetail();

        LocalDate localDate = LocalDate.now().plusDays(2);


        List<Slot> slotsOnDayForService = getSlotsOnDayForService(salonServiceDetail, localDate);
        slotsOnDayForService.stream()
                .limit(2)
                .forEach(this::saveSlotWithConfirmed);


        List<Slot> results = getSlotsOnDayForService(getASalonServiceDetail(), localDate);
        assertResults(localDate, results);
        assertThat(results.size(), not(slotsOnDayForService.size()));

    }

    private void saveSlotWithConfirmed(Slot slot) {
        slot.setStatus(SlotStatus.CONFIRMED);
        slotRepository.save(slot);
    }

    private List<Slot> getSlotsOnDayForService(SalonServiceDetail salonServiceDetail, LocalDate localDate) {
        LocalDateTime startDate = localDate.atTime(0, 1);
        LocalDateTime endDate = localDate.atTime(23, 59);
        return slotRepository.findAllBySlotForGreaterThanEqualAndSlotForLessThanEqualAndAvailableServicesContainingAndStatus(startDate, endDate, salonServiceDetail, SlotStatus.AVAILABLE);
    }
}