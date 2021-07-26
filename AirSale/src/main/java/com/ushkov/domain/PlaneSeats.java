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
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "plane_seats")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
public class PlaneSeats {
    @Id
    @Column(name = "plane_seats_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plane", nullable = false)
    @JsonBackReference
    private Plane plane;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "seat_class", nullable = false)
    @JsonBackReference
    private SeatClass seat;
    @Column(name = "number_of_seats", nullable = false)
    private short numberOfSeats;

    @OneToMany(mappedBy = "seat", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Ticket> tickets = Collections.emptySet();

}
