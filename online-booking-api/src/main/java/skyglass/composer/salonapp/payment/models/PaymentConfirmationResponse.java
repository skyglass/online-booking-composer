package skyglass.composer.salonapp.payment.models;

import lombok.Data;
import skyglass.composer.salonapp.config.SalonDetails;
import skyglass.composer.salonapp.ticket.Ticket;

@Data
public class PaymentConfirmationResponse {
    Ticket ticket;
    SalonDetails salonDetails;
}
