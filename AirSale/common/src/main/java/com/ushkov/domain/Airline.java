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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "airline")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"flights", "planes"})
@JsonIgnoreProperties(value = {"flights", "planes"})
public class Airline {
    @Id
    @Column(name = "airline_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @Column(name = "airline_name", length = 100, nullable = false, unique = true)
    private String name;
    @Column(name = "airline_short_name", length = 3, nullable = false, unique = true)
    private String shortName;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Flight> flights = Collections.emptySet();

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "airline_plane",
                joinColumns = @JoinColumn(name = "airline"),
                inverseJoinColumns = @JoinColumn(name = "plane"))
    @JsonIgnoreProperties("airlines")
    private Set<Plane> planes = Collections.emptySet();


}
