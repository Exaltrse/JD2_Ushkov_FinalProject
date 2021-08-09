package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_passenger")
@Cacheable("maincache")
@Data
@NoArgsConstructor
public class UserPassenger {
    @Id
    @Column(name = "user_passenger")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user", nullable = false)
    private int user;
    @Column(name = "passenger", nullable = false)
    private long passenger;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;
}
