package com.ushkov.domain;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.cache.annotation.Cacheable;

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
    @Column(name = "airpot_location", length = 100, nullable = false, unique = true)
    private String location;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "departure", fetch = FetchType.EAGER)
    @JsonBackReference
    Set<Flight> departure = Collections.emptySet();

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "destination", fetch = FetchType.EAGER)
    @JsonBackReference
    Set<Flight> destination = Collections.emptySet();


}
