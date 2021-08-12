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
@ApiModel(description = "Entity, that represent Country.")
public class CountryDTO {
    @ApiModelProperty(
            value = "Primary key. ID of Country in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Country entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Country entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Country entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Country entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Ful name of Country.",
            example = "Nigeria",
            required = true,
            position = 1)
    @Size(max = 150, message = "Max length of country name is 150 chars.")
    @NotBlank(message = "Name of country must be at least 1 symbol")
    private String name;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 2)
    @NotNull(message = "Disabled flag of Country entity can`t be NULL.")
    private boolean disabled;
}
