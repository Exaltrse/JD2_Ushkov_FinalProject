package com.ushkov.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(description = "Entity, that represent that represent joining table for entities of Passenger and Passport.")
public class PassengerPassportDTO {

    @ApiModelProperty(
            value = "Primary key. ID of PassengerPassport in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new PassengerPassport entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing PassengerPassport entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of PassengerPassport entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of PassengerPassport entity can be " + Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "ID of Passenger entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Passenger entity in PassengerPassport entity can`t be NULL.")
    @Min(value = 1, message = "ID of Passenger entity in PassengerPassport entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            message = "Max value of ID of Passenger entity in PassengerPassport entity can be " + Long.MAX_VALUE + ".")
    private Long passenger;

    @ApiModelProperty(
            value = "ID of Passport entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of Passport entity in PassengerPassport entity can`t be NULL.")
    @Min(value = 1, message = "ID of Passport entity in PassengerPassport entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            message = "Max value of ID of Passport entity in PassengerPassport entity can be " + Long.MAX_VALUE + ".")
    private Long passport;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of PassengerPassport entity can`t be NULL.")
    private boolean disabled;
}
