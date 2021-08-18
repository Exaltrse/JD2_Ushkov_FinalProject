package com.ushkov.validation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode
@ToString
public class Violation {
    private String fieldName;

    private String message;


    public Violation(String field, String defaultMessage) {
        this.fieldName = field;
        this.message = defaultMessage;
    }
}
