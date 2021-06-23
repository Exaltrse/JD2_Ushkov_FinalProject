package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "current_flight_status")
@Embeddable
@Data
@NoArgsConstructor
public class CurrentFlightStatus {
    @Id
    @Column(name = "current_flight_status_id")
    @SequenceGenerator(name = "currentFlightStatusSequenceGenerator", sequenceName = "current_flight_status_current_flight_status_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "currentFlightStatusSequenceGenerator")
    private short id;
    @Column(name = "current_flight_status_name", length = 20, nullable = false, unique = true)
    private String name;
}
