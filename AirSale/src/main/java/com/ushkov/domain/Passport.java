package com.ushkov.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Generated;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "passport")
@Embeddable
@Data
@NoArgsConstructor
public class Passport {
    @Id
    @SequenceGenerator(name = "passportSequence", sequenceName = "passport_passport_id_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "passportSequence")
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
    @Column(name = "citizenship", nullable = false)
    @Embedded
    private Country citizenship;

}
