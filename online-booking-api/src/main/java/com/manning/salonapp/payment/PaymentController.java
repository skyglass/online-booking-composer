package com.manning.salonapp.payment;


import com.manning.salonapp.common.SalonException;
import com.manning.salonapp.config.SalonDetails;
import com.manning.salonapp.payment.models.Payment;
import com.manning.salonapp.payment.models.PaymentConfirmationResponse;
import com.manning.salonapp.payment.models.PaymentRequest;
import com.manning.salonapp.ticket.Ticket;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolationException;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentController {

    PaymentService paymentService;
    SalonDetails salonDetails;

    public PaymentController(PaymentService paymentService, SalonDetails salonDetails) {
        this.paymentService = paymentService;
        this.salonDetails = salonDetails;
    }


    @PostMapping("/initiate")
    @ApiOperation(value = "InitiatePaymentAPI")

    public Payment initiatePayment(@RequestBody PaymentRequest paymentRequest) {

        try {
            return paymentService.initiate(paymentRequest);
        } catch (ConstraintViolationException e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        } catch (SalonException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }

    }

    @PutMapping("/confirm/{id}")
    @ApiOperation(value = "VerifyPaymentAndConfirmSlotAPI")
    public PaymentConfirmationResponse verifyPaymentAndConfirmSlotAPI(@PathVariable Long id) {

        try {

            Ticket ticket = paymentService.confirm(id);

            PaymentConfirmationResponse paymentConfirmationResponse = new PaymentConfirmationResponse();
            paymentConfirmationResponse.setSalonDetails(salonDetails.clone());
            paymentConfirmationResponse.setTicket(ticket);
            return paymentConfirmationResponse;

        } catch (SalonException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }


    }


}
