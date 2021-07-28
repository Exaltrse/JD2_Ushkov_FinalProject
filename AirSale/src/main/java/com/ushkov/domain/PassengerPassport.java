package com.ushkov.domain;

import jdk.jfr.Enabled;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Enabled
@Table(name = "passenger_passport")
@Cacheable("maincache")
@Data
@NoArgsConstructor
public class PassengerPassport {
    @Id
    @Column(name = "passenger_passport_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @Column(name = "passenger", nullable = false)
    private long passenger;
    @Column(name = "passport", nullable = false)
    private long passport;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
}
