package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "current_flight")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
public class CurrentFlight {
    @Id
    @Column(name = "current_flight_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_plane", nullable = false)
    @JsonBackReference
    private FlightPlane flightPlane;
    @Column(name = "departure_date", nullable = false)
    private Timestamp departureDate;
    @Column(name = "arrival_date", nullable = false)
    private Timestamp arrivalDate;
    @Column(name = "baseprice", nullable = false)
    //TODO: Think about this type
    private BigDecimal basePrice;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "current_flight_status", nullable = false)
    @JsonBackReference
    private CurrentFlightStatus currentFlightsStatus;
    @Column(name = "is_expired", nullable = false)
    private boolean isExpired;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight", nullable = false)
    @JsonBackReference
    private Flight flight;

    @OneToMany(mappedBy = "currentFlight", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Ticket> tickets = Collections.emptySet();

}
