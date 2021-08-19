package com.ushkov.requests;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Validated
@ApiModel(description = "")
public class NameLastNameRequest {
    private String firstName;

    private String lastName;
}
