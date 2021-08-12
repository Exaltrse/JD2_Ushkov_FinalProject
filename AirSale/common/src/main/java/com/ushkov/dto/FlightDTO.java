package com.ushkov.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

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
@ApiModel(description = "Entity, that represent Flight.")
public class FlightDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Flight in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Flight entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Flight entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Flight entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Discount entity can be " + Integer.MAX_VALUE + ".")
    private Integer id;

    @ApiModelProperty(
            value = "Number of flight. Typically alphanumerical.",
            example = "AV7589",
            required = true,
            position = 1)
    @Size(min = 2, max = 7, message = "Length of number of Flight must be between 2 and 7 chars.")
    @NotBlank(message = "Number of Flight must be at least 2 symbol.")
    private String number;

    @ApiModelProperty(
            value = "ID of Airline entity, operating Flight.",
            required = true,
            position = 2)
    @NotNull(message = "ID of Airline entity in Flight entity can`t be NULL.")
    @Min(value = 1, message = "ID of Airline entity in Flight entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Airline entity in Flight entity can be " + Short.MAX_VALUE + ".")
    private Short airline;

    @ApiModelProperty(
            value = "ID of entity of Airport of departure.",
            required = true,
            position = 3)
    @NotNull(message = "ID of Airport entity of departure in Flight entity can`t be NULL.")
    @Min(value = 1, message = "ID of Airport of departure entity in Flight entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Airport of departure entity in Flight entity can be " + Short.MAX_VALUE + ".")
    private Short departure;

    @ApiModelProperty(
            value = "ID of entity of Airport of arrival.",
            required = true,
            position = 3)
    @NotNull(message = "ID of Airport entity of arrival in Flight entity can`t be NULL.")
    @Min(value = 1, message = "ID of Airport of arrival entity in Flight entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Airport of arrival entity in Flight entity can be " + Short.MAX_VALUE + ".")
    private Short destination;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 7)
    @NotNull(message = "Disabled flag of Flight entity can`t be NULL.")
    private boolean disabled;

}
