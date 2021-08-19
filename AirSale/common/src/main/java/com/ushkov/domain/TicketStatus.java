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
@Table(name = "ticket_status")
@Cacheable("maincache")
@Data
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"tickets"})
public class TicketStatus {
    @Id
    @Column(name = "ticket_status_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Short id;
    @Column(name = "ticket_status_name", length = 20, nullable = false, unique = true)
    private String name;
    @Column(name = "disabled", nullable = false)
    private boolean disabled;

    @NotFound(action = NotFoundAction.IGNORE)
    @OneToMany(mappedBy = "ticketStatus", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonBackReference
    private Set<Ticket> tickets = Collections.emptySet();
}
