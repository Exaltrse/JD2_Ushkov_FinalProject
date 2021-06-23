package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "user_passenger")
@Data
@NoArgsConstructor
public class UserPassenger {
    @Id
    @Column(name = "user_passenger")
    @SequenceGenerator(name = "userPassengerSequenceGenerator", sequenceName = "user_passenger_user_passenger_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userPassengerSequenceGenerator")
    private long id;
    @Column(name = "user", nullable = false)
    @Embedded
    private User user;
    @Column(name = "passenger", nullable = false)
    @Embedded
    private Passenger passenger;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
