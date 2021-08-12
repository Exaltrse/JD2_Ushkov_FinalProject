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
@ApiModel(description = "Entity, that represent Passenger Class.")
public class PassengerClassDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Passenger Class in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new PassengerClass entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing PassengerClass entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of PassengerClass entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of PassengerClass entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Name of Passenger Class.",
            example = "VIP",
            required = true,
            position = 1)
    @Size(max = 30, message = "Max length of PassengerClass name is 30 chars.")
    @NotBlank(message = "Name of PassengerClass must be at least 1 symbol")
    private String name;

    @ApiModelProperty(
            value = "Properties of Passenger class",
            example = "AllInclusive",
            required = true,
            position = 2)
    @Size(max = 40, message = "Max length of PassengerClass properties is 40 chars.")
    @NotBlank(message = "Properties of PassengerClass must be at least 1 symbol")
    private String properties;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of PassengerClass entity can`t be NULL.")
    private boolean disabled;

}
