package com.manning.salonapp.payment;


import com.manning.salonapp.common.SalonException;

import com.manning.salonapp.payment.models.Payment;
import com.manning.salonapp.payment.models.PaymentRequest;
import com.manning.salonapp.payment.models.PaymentStatus;
import com.manning.salonapp.salonservice.SalonService;
import com.manning.salonapp.salonservice.SalonServiceDetail;
import com.manning.salonapp.slot.Slot;
import com.manning.salonapp.slot.SlotService;
import com.manning.salonapp.ticket.Ticket;
import com.manning.salonapp.ticket.TicketService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentRetrieveParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.time.LocalDateTime;

@Service
@Slf4j
@Validated
public class PaymentService {


    @Autowired
    PaymentRepository paymentRepository;

    @Autowired
    SalonService salonService;

    @Autowired
    SlotService slotService;
    @Autowired
    TicketService ticketService;

    @Value("${stripe.secret}")
    String stripeApiKey;


    @PostConstruct
    public void onBeansLoaded(){
        Stripe.apiKey =stripeApiKey;
    }


    public void validatePayment(String intentId){


        try {
            PaymentIntent intent = PaymentIntent.retrieve(intentId);
            if(intent.getStatus().equalsIgnoreCase("succeeded") == false)
                throw new SalonException("Payment is not succeeded,Invalid Entry" );

            log.info(intent.toJson());
        } catch (StripeException e) {
            e.printStackTrace();
            throw new SalonException(e.getMessage());
        }

    }


    public PaymentIntent createPayment(Payment payment){

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setCurrency("usd")
                        .setDescription("for Slot " + payment.getSlot().getId())
                        .setReceiptEmail(payment.getEmail())
                        .setAmount((payment.getAsCents()))
                        .putMetadata("integration_check", "accept_a_payment")
                        .build();

        try {
            PaymentIntent intent = PaymentIntent.create(params);
            return intent;
        } catch (StripeException e) {
            e.printStackTrace();
            throw new SalonException(e.getMessage());
        }

    }




    public Payment hasExistingPayment(PaymentRequest paymentRequest) {
        Slot slot = slotService.findLockedSlotId(paymentRequest.getSlotId()).orElse(null);
        if(null == slot)
            return null;



        return   paymentRepository.findBySlotAndEmail(slot,paymentRequest.getEmail());

    }
    public Payment initiate(@Valid PaymentRequest paymentRequest) {

        Payment existingPayment = hasExistingPayment(paymentRequest);

        if(null != existingPayment)
            return existingPayment;

        Slot slot = slotService.findAvailableSlotId(paymentRequest.getSlotId()).orElseThrow(() -> new SalonException("Invalid Slot ID Or Slot Not Available"));
        SalonServiceDetail serviceDetail = salonService.findById(paymentRequest.getSelectedSalonServiceDetailId()).orElseThrow(() -> new SalonException("Invalid Salon Service ID"));

        Payment payment = asPayment(paymentRequest, slot, serviceDetail);

        PaymentIntent paymentIntent = createPayment(payment);

        log.info("paymentIntent" + paymentIntent.toJson());

        payment.setClientSecret(paymentIntent.getClientSecret());
        payment.setIntentId(paymentIntent.getId());

        slotService.setToLockedWithService(slot, serviceDetail);
        paymentRepository.save(payment);
        return payment;
    }


    @Transactional
    public Ticket confirm(Long paymentRequestId) {

        Payment payment = paymentRepository.findById(paymentRequestId).orElseThrow(() -> new SalonException("Invalid Payment ID "));

        validatePayment(payment.getIntentId());
        slotService.setToConfirmed(payment.getSlot());
        payment.setStatus(PaymentStatus.SUCCESS);
        paymentRepository.save(payment);

        return ticketService.book(payment);
    }


    public Payment asPayment(PaymentRequest paymentRequest, Slot slot, SalonServiceDetail serviceDetail) {
        Payment payment = new Payment();
        payment.setAmount(serviceDetail.getPrice());
        payment.setFirstName(paymentRequest.getFirstName());
        payment.setLastName(paymentRequest.getLastName());
        payment.setEmail(paymentRequest.getEmail());
        payment.setPhoneNumber(paymentRequest.getPhoneNumber());
        payment.setSlot(slot);
        payment.setCreated(LocalDateTime.now());
        payment.setSelectedService(serviceDetail);
        return payment;

    }

}
