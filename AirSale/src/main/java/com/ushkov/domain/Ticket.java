package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "passport", nullable = false)
    private long passport;
    @Column(name = "current_flight", nullable = false)
    private long currentFlight;
    @Column(name = "discount", nullable = false)
    private short discount;
    //TODO: Think about this type
    @Column(name = "final_price", nullable = false)
    private BigDecimal finalPrice;
    @Column(name = "ticket_status")
    private int ticket_status;
    @Column(name = "seat_class", nullable = false)
    private int seatClass;

}
