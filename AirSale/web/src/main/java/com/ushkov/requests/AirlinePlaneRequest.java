package com.ushkov.requests;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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
public class AirlinePlaneRequest {
    @ApiModelProperty(
            value = "ID of Plane entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Plane entity can`t be NULL.")
    @Min(value = 1, message = "ID of Plane entity must be positive number.")
    @Max(value = Integer.MAX_VALUE, message = "Max value of ID of Plane entity can be " + Integer.MAX_VALUE + ".")
    private Integer plane;

    @ApiModelProperty(
            value = "ID of Airline entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of Airline entity can`t be NULL.")
    @Min(value = 1, message = "ID of Airline entity must be positive number.")
    @Max(value = Short.MAX_VALUE, message = "Max value of ID of Airline entity can be " + Short.MAX_VALUE + ".")
    private Short airline;
}
