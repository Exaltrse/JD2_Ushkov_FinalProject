package com.ushkov.domain;

import java.util.Collections;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "passenger")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"users", "passports"})
public class Passenger {
    @Id
    @Column(name = "passenger_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", length = 100, nullable = false)
    private String firstName;
    @Column(name = "last_name", length = 100, nullable = false)
    private String lastName;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "passenger_class", nullable = false)
    @JsonManagedReference
    private PassengerClass passengerClass;
    @Column(name = "comments", length = 100)
    private String comments;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(mappedBy = "passengers", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("passengers")
    private Set<Users> users = Collections.emptySet();

    @NotFound(action = NotFoundAction.IGNORE)
    @ManyToMany(mappedBy = "passengers", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("passengers")
    private Set<Passport> passports = Collections.emptySet();

}
