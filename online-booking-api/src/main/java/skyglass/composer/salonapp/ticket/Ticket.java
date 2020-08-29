package skyglass.composer.salonapp.ticket;

import lombok.Getter;
import lombok.Setter;
import skyglass.composer.salonapp.payment.models.Payment;

import javax.persistence.*;
@Entity
@Getter
@Setter
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @OneToOne
    private Payment payment;

    TicketStatus ticketStatus=TicketStatus.BOOKED;



}
