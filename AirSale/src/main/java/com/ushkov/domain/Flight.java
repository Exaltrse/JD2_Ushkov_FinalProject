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

@Entity
@Table(name = "flight")
@Embeddable
@Data
@NoArgsConstructor
public class Flight {
    @Id
    @Column(name = "flight_id")
    @SequenceGenerator(name = "flightSequenceGenerator", sequenceName = "flight_flight_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flightSequenceGenerator")
    private int id;
    @Column(name = "flight_number", length = 7, nullable = false)
    private String number;
    @Column(name = "airline", nullable = false)
    @Embedded
    private Airline airline;
    @Column(name = "airport_departure", nullable = false)
    @Embedded
    private Airport departure;
    @Column(name = "airport_destination", nullable = false)
    @Embedded
    private Airport destination;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;


}
