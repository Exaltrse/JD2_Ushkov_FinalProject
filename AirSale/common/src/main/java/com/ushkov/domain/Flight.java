package com.ushkov.domain;

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
@Table(name = "flight")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"currentFlights", "flightPlanes"}) //"planes",
public class Flight {
    @Id
    @Column(name = "flight_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "flight_number", length = 7, nullable = false)
    private String number;
    @ManyToOne
    @JoinColumn(name = "airline", nullable = false)
    @JsonManagedReference
    private Airline airline;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airport_departure", nullable = false)
    @JsonManagedReference
    private Airport departure;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "airport_destination", nullable = false)
    @JsonManagedReference
    private Airport destination;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

//    @ManyToMany(fetch = FetchType.EAGER)
//    @JoinTable( name = "flight_plane",
//            joinColumns = @JoinColumn(name = "flight"),
//            inverseJoinColumns = @JoinColumn(name = "plane"))
//    @JsonIgnoreProperties("flights")
//    private Set<Plane> planes = Collections.emptySet();

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<CurrentFlight> currentFlights = Collections.emptySet();

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "flight", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<FlightPlane> flightPlanes = Collections.emptySet();
}
