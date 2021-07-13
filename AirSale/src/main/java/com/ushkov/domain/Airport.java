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
@Table(name = "airport")
@Data
@NoArgsConstructor
public class Airport {
    @Id
    @Column(name = "airport_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private short id;
    @Column(name = "airport_name", length = 200, nullable = false, unique = true)
    private String name;
    @Column(name = "airport_short_name", length = 4, nullable = false, unique = true)
    private String shortName;
    //TODO: make JSON with coordinates
    @Column(name = "airpot_location", length = 100, nullable = false, unique = true)
    private String location;
}
