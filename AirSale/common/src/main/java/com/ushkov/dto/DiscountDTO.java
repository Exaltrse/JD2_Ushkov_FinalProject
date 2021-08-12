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
@ApiModel(description = "Entity, that represent Discount.")
public class DiscountDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Discount in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Discount entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Discount entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Discount entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Discount entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Name of Discount.",
            example = "20%",
            required = true,
            position = 1)
    @Size(max = 30, message = "Max length of Discount name is 30 chars.")
    @NotBlank(message = "Discount name must be at least 1 symbol.")
    private String name;

    @ApiModelProperty(
            value = "Value of Discount in percent.",
            example = "20",
            required = true,
            position = 2)
    @NotNull(message = "Value of Discount entity can`t be NULL.")
    @Min(value = 0, message = "Value of Discount entity must be positive number or 0.")
    @Max(value = 100, message = "Max value of value of Discount entity can be 100")
    private Short value;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 7)
    @NotNull(message = "Disabled flag of Discount entity can`t be NULL.")
    private boolean disabled;

}
