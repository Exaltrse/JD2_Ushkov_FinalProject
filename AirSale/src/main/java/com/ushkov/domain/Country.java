package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "country")
@Embeddable
@Data
@NoArgsConstructor
public class Country {
    @Id
    @Column(name = "country_id")
    @SequenceGenerator(name = "countrySequenceGenerator", sequenceName = "country_country_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "countrySequenceGenerator")
    private int id;
    @Column(name = "country_name", length = 150, nullable = false, unique = true)
    private String name;

}
