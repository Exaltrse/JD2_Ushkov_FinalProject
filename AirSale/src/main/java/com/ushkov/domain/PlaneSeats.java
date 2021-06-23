package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "plane_seats")
@Embeddable
@Data
@NoArgsConstructor
public class PlaneSeats {
    @Id
    @Column(name = "plane_seats_id")
    @SequenceGenerator(name = "planeSeatsSequenceGenerator", sequenceName = "plane_seats_plane_seats_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "planeSeatsSequenceGenerator")
    private int id;
    @Column(name = "plane", nullable = false)
    private int plane;
    @Column(name = "seat_class")
    @Embedded
    private SeatClass seatClass;
    @Column(name = "number_of_seats", nullable = false)
    private short numberOfSeats;

}
