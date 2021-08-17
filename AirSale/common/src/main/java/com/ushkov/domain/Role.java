package com.ushkov.domain;

import java.util.Collections;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.cache.annotation.Cacheable;

@Entity
@Table(name = "roles")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"users"})
public class Role {
    @Id
    @Column(name = "roles_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @Column(name = "role_name", length = 20, nullable = false, unique = true)
    private String name;
    @Column(name = "permissions")
    private String permissions;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Users> users = Collections.emptySet();

}
