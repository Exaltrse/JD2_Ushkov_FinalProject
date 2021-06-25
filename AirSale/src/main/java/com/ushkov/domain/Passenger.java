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
@Table(name = "passenger")
@Embeddable
@Data
@NoArgsConstructor
public class Passenger {
    @Id
    @Column(name = "passenger_id")
    @SequenceGenerator(name = "passengerSequenceGenerator", sequenceName = "passanger_passanger_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passengerSequenceGenerator")
    private long id;
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;
    @Column(name = "passenger_class", nullable = false)
    @Embedded
    private PassengerClass passengerClass;
    @Column(name = "comments", length = 100)
    private String comments;

}
