package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "airline")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"flights", "planes"})
@JsonIgnoreProperties(value = {"flights", "planes"})
//@Validated
//@Schema(description = "Entity, that represent Arline")
public class Airline {
    @Id
    @Column(name = "airline_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Schema(description = "Primary key. ID")
    private Short id;
    @Column(name = "airline_name", length = 100, nullable = false, unique = true)
    //@Schema(description = "Full name of Airline.")
    //@Size(max = 100)
    //@NotBlank
    private String name;
    @Column(name = "airline_short_name", length = 3, nullable = false, unique = true)
    //@Schema(description = "Short name of Airline.")
    //@Size(min = 2, max = 3)
    //@NotBlank
    private String shortName;
    @Column(name = "disabled", nullable = false)
    //@Schema(description = "Represent if that entity can be used or it disabled in system.")
    private boolean disabled;

    @OneToMany(mappedBy = "airline", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    //@Schema(hidden = true)
    private Set<Flight> flights = Collections.emptySet();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "airline_plane",
                joinColumns = @JoinColumn(name = "airline"),
                inverseJoinColumns = @JoinColumn(name = "plane"))
    @JsonIgnoreProperties("airlines")
    //@Schema(hidden = true)
    private Set<Plane> planes = Collections.emptySet();


}
