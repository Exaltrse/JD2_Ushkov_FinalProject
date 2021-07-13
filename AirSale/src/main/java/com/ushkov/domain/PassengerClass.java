package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "passenger_class")
@Data
@NoArgsConstructor
public class PassengerClass {
    @Id
    @Column(name = "passenger_class_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private short id;
    @Column(name = "passenger_class_name", length = 30, nullable = false, unique = true)
    private String name;
    @Column(name = "passenger_class_properties", nullable = false)
    //TODO: Think about valuable properties and create class
    private String properties;

}
