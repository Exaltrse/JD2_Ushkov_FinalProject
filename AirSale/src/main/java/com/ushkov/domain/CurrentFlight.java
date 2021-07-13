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
import java.sql.Timestamp;

@Entity
@Table(name = "current_flight")
@Data
@NoArgsConstructor
public class CurrentFlight {
    @Id
    @Column(name = "current_flight_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;

    //TODO: Refactor flight and plane
    @Column(name = "flight_plane", nullable = false)
    private Long flight;
    @Column(name = "departure_date", nullable = false)
    //TODO: Change Date type
    private Timestamp departureDate;
    @Column(name = "arrival_date", nullable = false)
    //TODO: Change Date type
    private Timestamp arrivalDate;
    @Column(name = "baseprise", nullable = false)
    //TODO: Think about this type
    private BigDecimal basePrice;
    @Column(name = "current_flight_status", nullable = false)
    private Integer currentFlightStatus;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

}
