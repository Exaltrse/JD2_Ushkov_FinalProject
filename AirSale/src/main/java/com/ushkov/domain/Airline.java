package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "airline")
@Embeddable
@Data
@NoArgsConstructor
public class Airline {
    @Id
    @Column(name = "airline_id")
    @SequenceGenerator(name = "airlineSequenceGenerator", sequenceName = "Airline_airline_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "airlineSequenceGenerator")
    private Short id;
    @Column(name = "airline_name", length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "airline_short_name", length = 3, nullable = false, unique = true)
    private String shortName;

}
