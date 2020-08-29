package skyglass.salonapp.payment.models;


import lombok.*;
import skyglass.salonapp.salonservice.SalonServiceDetail;
import skyglass.salonapp.slot.Slot;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;



    @ManyToOne(fetch = FetchType.EAGER)
    private SalonServiceDetail selectedService;

    @ManyToOne(fetch = FetchType.EAGER)
    private Slot slot;


    private Long amount;

    private LocalDateTime created;
    private LocalDateTime updated;
    private PaymentStatus status;

    private String clientSecret;
    private String intentId;



    private String firstName;


    private String email;


    private String lastName;


    private String phoneNumber;


    public String getName() {
        return firstName + " " + lastName;
    }
    public long getAsCents() {
        return amount * 100;
    }
}
