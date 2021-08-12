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
@Table(name = "user_passenger")
@Cacheable("maincache")
@Data
@NoArgsConstructor
public class UserPassenger {
    @Id
    @Column(name = "user_passenger")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "user_id", nullable = false)
    private int user;
    @Column(name = "passenger", nullable = false)
    private long passenger;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;
}
