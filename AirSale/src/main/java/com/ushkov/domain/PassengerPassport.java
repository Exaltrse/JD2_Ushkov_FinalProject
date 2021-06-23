package com.ushkov.domain;

import jdk.jfr.Enabled;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Enabled
@Table(name = "passenger_passport")
@Embeddable
@Data
@NoArgsConstructor
public class PassengerPassport {
    @Id
    @Column(name = "passenger_passport_id")
    @SequenceGenerator(name = "passengerPassportSequenceGenerator", sequenceName = "passenger_passport_passenger_passport_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passengerPassportSequenceGenerator")
    private long id;
    @Column(name = "passenger", nullable = false)
    @Embedded
    private Passenger passenger;
    @Column(name = "passport", nullable = false)
    @Embedded
    private Passport passport;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
