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

@Entity
@Table(name = "current_flight_status")
@Data
@NoArgsConstructor
public class CurrentFlightStatus {
    @Id
    @Column(name = "current_flight_status_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private short id;
    @Column(name = "current_flight_status_name", length = 20, nullable = false, unique = true)
    private String name;
}
