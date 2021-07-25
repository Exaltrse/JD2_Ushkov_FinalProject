package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket")
@Data
@NoArgsConstructor
public class Ticket {
    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passport", nullable = false)
    @JsonBackReference
    private Passport passport;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_flight", nullable = false)
    @JsonBackReference
    private CurrentFlight currentFlight;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount", nullable = false)
    @JsonBackReference
    private Discount discount;
    //TODO: Think about this type
    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_status", nullable = false)
    @JsonBackReference
    private TicketStatus ticketStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat", nullable = false)
    @JsonBackReference
    private PlaneSeats seat;

}
