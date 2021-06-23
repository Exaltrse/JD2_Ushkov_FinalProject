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
@Table(name = "ticket_status")
@Embeddable
@Data
@NoArgsConstructor
public class TicketStatus {
    @Id
    @Column(name = "ticket_status_id")
    @SequenceGenerator(name = "ticketStatusSequenceGenerator", sequenceName = "ticket_status_ticket_status_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticketStatusSequenceGenerator")
    private short id;
    @Column(name = "ticket_status_name", length = 20, nullable = false, unique = true)
    private String name;
}
