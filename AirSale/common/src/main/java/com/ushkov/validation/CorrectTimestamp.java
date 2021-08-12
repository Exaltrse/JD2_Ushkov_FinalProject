package com.ushkov.validation;


import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Timestamp;
import java.util.Optional;

import javax.validation.Constraint;


@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CorrectTimestampValidator.class)
@Documented
public @interface CorrectTimestamp {

    String message() default "Incorrect data of timestamp fields.";

    Optional<Timestamp> startDate();

    Object endDate();

}