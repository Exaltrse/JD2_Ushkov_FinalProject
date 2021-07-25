package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "passport")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"passengers", "tickets"})
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "passport_id")
    private long id;
    @Column(name = "first_name_latin", length = 100, nullable = false)
    private String firstNameLatin;
    @Column(name = "last_name_latin", length = 100, nullable = false)
    private String lastNameLatin;
    @Column(name = "series", length = 12, nullable = false)
    private String series;
    @Column(name = "expire_date", nullable = false)
    //TODO: Change type of Data
    private Date expire_date;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citizenship", nullable = false)
    @JsonBackReference
    private Country citizenship;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "passenger_passport",
            joinColumns = @JoinColumn(name = "passport"),
            inverseJoinColumns = @JoinColumn(name = "passenger"))
    @JsonIgnoreProperties("passports")
    private Set<Passenger> passengers = Collections.emptySet();

    @OneToMany(mappedBy = "passport", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Ticket> tickets = Collections.emptySet();
}
