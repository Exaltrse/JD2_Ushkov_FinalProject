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
@Table(name = "user")
@Embeddable
@Data
@NoArgsConstructor
public class User {
    @Id
    @Column(name = "user_id")
    @SequenceGenerator(name = "userSequenceGenerator", sequenceName = "users_user_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSequenceGenerator")
    private int id;
    @Column(name = "user_role", nullable = false)
    @Embedded
    private Role role;
    @Column(name = "login", length = 40, unique = true, nullable = false)
    private String login;
    @Column(name = "password", length = 140, nullable = false)
    private String password;
    //TODO: Add list of passengers
}
