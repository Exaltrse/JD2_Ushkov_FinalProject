package com.ushkov.domain;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "seat_class")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"planeSeats"})
public class SeatClass {
    @Id
    @Column(name = "seat_class_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private short id;
    @Column(name = "seat_class_name", length = 30, nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "seat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<PlaneSeats> planeSeats = Collections.emptySet();
}
