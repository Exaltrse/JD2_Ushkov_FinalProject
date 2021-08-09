package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "flight_plane")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"currentFlights"})
public class FlightPlane {
    @Id
    @Column(name = "flight_plane_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight", nullable = false)
    @JsonManagedReference
    private Flight flight;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plane", nullable = false)
    @JsonManagedReference
    private Plane plane;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @OneToMany(mappedBy = "flightPlane", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<CurrentFlight> currentFlights = Collections.emptySet();
}