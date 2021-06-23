package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {
    @Id
    @Column(name = "ticket_id")
    @SequenceGenerator(name = "ticketSequenceGenerator", sequenceName = "ticket_ticket_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketSequenceGenerator")
    private long ticketId;
    @Column(name = "passport", nullable = false)
    private Passport passport;
    @Column(name = "current_flight", nullable = false)
    @Embedded
    private CurrentFlight currentFlight;
    @Column(name = "discount", nullable = false)
    @Embedded
    private Discount discount;
    //TODO: Think about this type
    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;
    @Column(name = "ticket_status")
    @Embedded
    private TicketStatus ticket_status;
    @Column(name = "seat_class", nullable = false)
    @Embedded
    private SeatClass seatClass;

}
