package skyglass.salonapp.payment.models;

import lombok.Data;
import skyglass.salonapp.config.SalonDetails;
import skyglass.salonapp.ticket.Ticket;

@Data
public class PaymentConfirmationResponse {
    Ticket ticket;
    SalonDetails salonDetails;
}
