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
@Table(name = "discount")
@Embeddable
@Data
@NoArgsConstructor
public class Discount {
    @Id
    @Column(name = "discount_id")
    @SequenceGenerator(name = "discountSequenceGenerator", sequenceName = "discount_discount_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "discountSequenceGenerator")
    private short id;
    @Column(name = "discount_name", length = 30, nullable = false, unique = true)
    private String name;
    @Column(name = "discount_value", nullable = false)
    private short value;

}
