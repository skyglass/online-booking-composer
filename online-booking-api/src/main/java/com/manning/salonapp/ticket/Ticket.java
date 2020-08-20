package com.manning.salonapp.ticket;

import com.manning.salonapp.payment.models.Payment;
import lombok.Getter;
import lombok.Setter;

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
