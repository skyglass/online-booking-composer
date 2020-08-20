package com.manning.salonapp.ticket;


import com.manning.salonapp.payment.models.Payment;
import com.manning.salonapp.salonservice.SalonService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TicketService {



    TicketRepository ticketRepository;


    SalonService salonService;

    public TicketService(TicketRepository ticketRepository, SalonService salonService) {
        this.ticketRepository = ticketRepository;
        this.salonService = salonService;
    }

    @Transactional
    public Ticket book( Payment payment) {
        Ticket ticket = new Ticket();
        ticket.setPayment(payment);


        return ticketRepository.save(ticket);
    }


    public Optional<Ticket> findById(Long id){
        return ticketRepository.findById(id);
    }


}
