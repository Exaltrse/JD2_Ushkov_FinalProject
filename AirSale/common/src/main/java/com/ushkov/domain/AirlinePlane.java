package com.ushkov.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "airline_plane")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class AirlinePlane {
    @Id
    @Column(name = "airline_plane_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "plane", nullable = false)
    private long plane;
    @Column(name = "airline", nullable = false)
    private short airline;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;
}
