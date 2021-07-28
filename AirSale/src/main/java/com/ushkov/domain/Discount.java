package com.ushkov.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.cache.annotation.Cacheable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "discount")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
public class Discount {
    @Id
    @Column(name = "discount_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private short id;
    @Column(name = "discount_name", length = 30, nullable = false, unique = true)
    private String name;
    @Column(name = "discount_value", nullable = false)
    private short value;

    @OneToMany(mappedBy = "discount", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<Ticket> tickets = Collections.emptySet();

}
