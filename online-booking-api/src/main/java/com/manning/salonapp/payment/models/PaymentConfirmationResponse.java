package com.manning.salonapp.payment.models;

import com.manning.salonapp.config.SalonDetails;
import com.manning.salonapp.ticket.Ticket;
import lombok.Data;

@Data
public class PaymentConfirmationResponse {
    Ticket ticket;
    SalonDetails salonDetails;
}
