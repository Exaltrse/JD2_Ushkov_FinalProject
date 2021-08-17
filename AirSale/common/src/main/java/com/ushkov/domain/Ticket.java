package com.ushkov.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "ticket")
@Cacheable("maincache")
@Data
@NoArgsConstructor
public class Ticket {
    @Id
    @Column(name = "ticket_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passport", nullable = false)
    @JsonManagedReference
    private Passport passport;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_flight", nullable = false)
    @JsonManagedReference
    private CurrentFlight currentFlight;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "discount", nullable = false)
    @JsonManagedReference
    private Discount discount;
    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ticket_status", nullable = false)
    @JsonManagedReference
    private TicketStatus ticketStatus;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat", nullable = false)
    @JsonManagedReference
    private PlaneSeats seat;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

}
