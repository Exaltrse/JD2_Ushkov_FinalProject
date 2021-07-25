package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "airline")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"flights", "planes"})
public class Airline {
    @Id
    @Column(name = "airline_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private short id;
    @Column(name = "airline_name", length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "airline_short_name", length = 3, nullable = false, unique = true)
    private String shortName;

    @OneToMany(mappedBy = "airline", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Flight> flights = Collections.emptySet();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "airline_plane",
                joinColumns = @JoinColumn(name = "airline"),
                inverseJoinColumns = @JoinColumn(name = "plane"))
    @JsonIgnoreProperties("airlines")
    private Set<Plane> planes = Collections.emptySet();

}
