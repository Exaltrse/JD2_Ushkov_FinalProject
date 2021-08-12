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
@ApiModel(description = "Entity, that represent that represent joining table for entities of Users and Passengers.")
public class UserPassengerDTO {

    @ApiModelProperty(
            value = "Primary key. ID of UserPassenger in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new UserPassenger entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing UserPassenger entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of existing UserPassenger entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of UserPassenger entity can be " + Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "ID of Users entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Users entity in UserPassenger entity can`t be NULL.")
    @Min(value = 1, message = "ID of Users entity in UserPassenger entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            message = "Max value of ID of Users entity in UserPassenger entity can be " + Integer.MAX_VALUE + ".")
    private Integer user;

    @ApiModelProperty(
            value = "ID of Passenger entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of Passenger entity in UserPassenger entity can`t be NULL.")
    @Min(value = 1, message = "ID of Passenger entity in UserPassenger entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            message = "Max value of ID of Passenger entity in UserPassenger entity can be " + Long.MAX_VALUE + ".")
    private long passenger;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of UserPassenger entity can`t be NULL.")
    private boolean disabled;
}
