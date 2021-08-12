package com.ushkov.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

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
@ApiModel(description = "Entity, that represent Passenger.")
public class PassengerDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Passenger in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Passenger entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Passenger entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Passenger entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Passenger entity can be " + Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "Name of Passenger",
            example = "Ivan",
            required = true,
            position = 1)
    @Size(max = 100, message = "Max length of Passenger name is 100 chars.")
    @NotBlank(message = "Name of Passenger must be at least 1 symbol")
    private String firstName;

    @ApiModelProperty(
            value = "Surname/lastname of Passenger",
            example = "Ivanov",
            required = true,
            position = 2)
    @Size(max = 100, message = "Max length of Passenger lastname is 100 chars.")
    @NotBlank(message = "Lastname of Passenger must be at least 1 symbol")
    private String lastName;

    @ApiModelProperty(
            value = "ID of PassengerClass entity.",
            required = true,
            position = 3)
    @NotNull(message = "ID of PassengerClass entity in Passenger entity can`t be NULL.")
    @Min(value = 1, message = "ID of PassengerClass entity in Passenger entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of PassengerClass entity in Passenger entity can be " + Short.MAX_VALUE + ".")
    private Short passengerClass;

    @ApiModelProperty(
            value = "Name of Passenger",
            example = "Diabetic",
            required = true,
            position = 4)
    @Size(max = 100, message = "Max length of Passenger`s comments is 100 chars.")
    @NotBlank(message = "Comments for Passenger must be at least 1 symbol")
    private String comments;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 5)
    @NotNull(message = "Disabled flag of Passenger entity can`t be NULL.")
    private boolean disabled;

}
