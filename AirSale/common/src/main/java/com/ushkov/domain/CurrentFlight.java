package com.ushkov.domain;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Set;

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

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "current_flight")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
public class CurrentFlight {
    @Id
    @Column(name = "current_flight_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_plane", nullable = false)
    @JsonManagedReference
    private FlightPlane flightPlane;
    @Column(name = "departure_date", nullable = false)
    private Timestamp departureDate;
    @Column(name = "arrival_date", nullable = false)
    private Timestamp arrivalDate;
    @Column(name = "baseprice", nullable = false)
    private BigDecimal basePrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_flight_status", nullable = false)
    @JsonManagedReference
    private CurrentFlightStatus currentFlightsStatus;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight", nullable = false)
    @JsonBackReference
    private Flight flight;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "currentFlight", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Ticket> tickets = Collections.emptySet();

}
