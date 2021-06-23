package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "current_flight")
@Embeddable
@Data
@NoArgsConstructor
public class CurrentFlight {
    @Id
    @Column(name = "current_flight_id")
    @SequenceGenerator(name = "currentFlightSequenceGenerator", sequenceName = "current_flight_current_flight_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currentFlightSequenceGenerator")
    private long id;

    //TODO: Refactor flight and plane
    @Column(name = "flight_plane", nullable = false)
    @Embedded
    private Flight flight;
    private Plane plane;
    @Column(name = "departure_date", nullable = false)
    //TODO: Change Date type
    private Date departureDate;
    @Column(name = "arrival_date", nullable = false)
    //TODO: Change Date type
    private Date arrivalDate;
    @Column(name = "baseprise", nullable = false)
    //TODO: Think about this type
    private BigDecimal basePrice;
    @Column(name = "current_flight_status", nullable = false)
    @Embedded
    private CurrentFlightStatus currentFlightStatus;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

}
