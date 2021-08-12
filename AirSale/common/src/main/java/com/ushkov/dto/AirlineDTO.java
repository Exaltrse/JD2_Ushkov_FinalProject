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
@ApiModel(description = "Entity, that represent Arline.")
public class AirlineDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Airline in DB. Can be NULL only for new entities.",
            required = false,
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Airline entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Airline entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Airline entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Airline entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Full name of Airline.",
            example = "British Airways",
            required = true,
            position = 1)
    @Size(max = 100, message = "Max length of full name of Airline is 100 chars.")
    @NotBlank(message = "Full name of Airline must be at least 1 symbol.")
    private String name;

    @ApiModelProperty(
            value = "Short name of Airline.",
            example = "BA",
            required = true,
            position = 2)
    @Size(min = 2, max = 3, message = "Length of short name of airline must be between 2 and 3 chars.")
    @NotBlank(message = "Short name of Airline must be at least 2 symbol.")
    private String shortName;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of Airline entity can`t be NULL.")
    private boolean disabled;



}
