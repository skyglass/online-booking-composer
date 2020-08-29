package skyglass.salonapp.ticket;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {

    TicketService ticketService;

    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @GetMapping("/{ticketId}")
    @ApiOperation(value = "VerifyTicketAPI")
    public Ticket verifyTicketAPI(@PathVariable Long ticketId) {

        return ticketService.findById(ticketId).orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST,"Invalid Ticket ID",null));
    }


}
