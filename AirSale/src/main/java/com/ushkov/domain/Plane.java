package com.ushkov.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "plane")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"airlines", "planeSeats", "flightPlanes"}) //"flights",
public class Plane {
    @Id
    @Column(name = "plane_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @Column(name = "aircraft_number", length = 9, nullable = false, unique = true)
    private String aircraftNumber;
    @Column(name = "properties", length = 100)
    private String properties;

    @ManyToMany(mappedBy = "planes", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("planes")
    private Set<Airline> airlines = Collections.emptySet();

//    @ManyToMany(mappedBy = "planes", fetch = FetchType.EAGER)
//    @JsonIgnoreProperties("planes")
//    private Set<Flight> flights = Collections.emptySet();

    @OneToMany(mappedBy = "plane", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<PlaneSeats> planeSeats = Collections.emptySet();


    @OneToMany(mappedBy = "plane", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<FlightPlane> flightPlanes = Collections.emptySet();

}
