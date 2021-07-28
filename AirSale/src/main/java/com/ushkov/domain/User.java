package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

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
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "user")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"passengers"})
public class User {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_role", nullable = false)
    @JsonBackReference
    private Role role;
    @Column(name = "login", length = 40, unique = true, nullable = false)
    private String login;
    @Column(name = "password", length = 140, nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable( name = "user_passenger",
            joinColumns = @JoinColumn(name = "user"),
            inverseJoinColumns = @JoinColumn(name = "passenger"))
    @JsonIgnoreProperties("users")
    private Set<Passenger> passengers = Collections.emptySet();
}
