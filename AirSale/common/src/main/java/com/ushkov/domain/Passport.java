package com.ushkov.domain;

import java.sql.Date;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "passport")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"passengers", "tickets"})
public class Passport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "passport_id")
    private Long id;
    @Column(name = "first_name_latin", length = 100, nullable = false)
    private String firstNameLatin;
    @Column(name = "last_name_latin", length = 100, nullable = false)
    private String lastNameLatin;
    @Column(name = "series", length = 12, nullable = false)
    private String series;
    @Column(name = "expire_date", nullable = false)
    private Date expireDate;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "citizenship", nullable = false)
    @JsonManagedReference
    private Country citizenship;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable( name = "passenger_passport",
            joinColumns = @JoinColumn(name = "passport"),
            inverseJoinColumns = @JoinColumn(name = "passenger"))
    @JsonIgnoreProperties("passports")
    private Set<Passenger> passengers = Collections.emptySet();

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "passport", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Ticket> tickets = Collections.emptySet();
}
