package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "airport")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"departure", "destination"})
public class Airport {
    @Id
    @Column(name = "airport_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @Column(name = "airport_name", length = 200, nullable = false, unique = true)
    private String name;
    @Column(name = "airport_short_name", length = 4, nullable = false, unique = true)
    private String shortName;
    //TODO: make JSON with coordinates
    @Column(name = "airpot_location", length = 100, nullable = false, unique = true)
    private String location;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @OneToMany(mappedBy = "departure", fetch = FetchType.EAGER)
    @JsonBackReference
    Set<Flight> departure = Collections.emptySet();

    @OneToMany(mappedBy = "destination", fetch = FetchType.EAGER)
    @JsonBackReference
    Set<Flight> destination = Collections.emptySet();


}
