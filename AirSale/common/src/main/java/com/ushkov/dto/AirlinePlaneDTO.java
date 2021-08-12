package com.ushkov.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import com.ushkov.validation.ValidationGroup.ExistingObject;
import com.ushkov.validation.ValidationGroup.NewObject;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Validated
@Builder
@ApiModel(description = "Entity, that represent joining table for entities of Airline and Plane.")
public class AirlinePlaneDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Airport in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new AirlinePlane entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing AirlinePlane entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of AirlinePlane entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of AirlinePlane entity can be " + Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "ID of Plane entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Plane entity can`t be NULL.")
    @Min(value = 1, message = "ID of Plane entity must be positive number.")
    @Max(value = Long.MAX_VALUE, message = "Max value of ID of Plane entity can be " + Long.MAX_VALUE + ".")
    private Long plane;

    @ApiModelProperty(
            value = "ID of Airline entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of Airline entity can`t be NULL.")
    @Min(value = 1, message = "ID of Airline entity must be positive number.")
    @Max(value = Short.MAX_VALUE, message = "Max value of ID of Airline entity can be " + Short.MAX_VALUE + ".")
    private Short airline;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of AirlineAirport entity can`t be NULL.")
    private boolean disabled;
}
