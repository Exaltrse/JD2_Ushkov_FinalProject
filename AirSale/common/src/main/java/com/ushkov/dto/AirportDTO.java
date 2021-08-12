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
@ApiModel(description = "Entity, that represent Airport.")
public class AirportDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Airport in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Airport entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Airport entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Airport entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Airport entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Ful name of Airport.",
            example = "Nnamdi Azikiwe International Airport",
            required = true,
            position = 1)
    @Size(max = 200, message = "Max length of full name is 200 chars.")
    @NotBlank(message = "Full name of Airport must be at least 1 symbol")
    private String name;

    @ApiModelProperty(
            value = "Short name of Airport.",
            example = "ABV",
            required = true,
            position = 2)
    @Size(min = 2, max = 3, message = "Length of short name of Airport must be between 2 and 3 chars.")
    @NotBlank(message = "Full name of Airport must be at least 2 symbol")
    private String shortName;

    @ApiModelProperty(
            value = "Location of Airport.",
            example = "Abuja, Nigeria",
            required = true,
            position = 3)
    @Size(max = 100,message = "Max length of location of airport is 100 chars.")
    @NotBlank(message = "Location of Airport must be at least 1 symbol")
    private String location;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 4)
    @NotNull(message = "Disabled flag of Airport entity can`t be NULL.")
    private boolean disabled;



}
