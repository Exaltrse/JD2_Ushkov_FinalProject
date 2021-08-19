package com.ushkov.requests;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Validated
@ApiModel(description = "")
public class CurrentFlightFindOneRequest {
    @ApiModelProperty(
            value = "Primary key. ID of CurrentFlight in DB. Can be NULL only for new entities.",
            position = 0)
    private Long id;

    private String timezoneid;
}
