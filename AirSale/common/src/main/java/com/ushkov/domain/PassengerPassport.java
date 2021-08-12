package com.ushkov.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "passenger_passport")
@Cacheable("maincache")
@Data
@NoArgsConstructor
public class PassengerPassport {
    @Id
    @Column(name = "passenger_passport_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "passenger", nullable = false)
    private long passenger;
    @Column(name = "passport", nullable = false)
    private long passport;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;
}
