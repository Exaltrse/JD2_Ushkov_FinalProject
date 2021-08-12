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
@ApiModel(description = "Entity, that represent Plane.")
public class PlaneDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Plane in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Plane entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Plane entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of existing Plane entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Plane entity can be " + Integer.MAX_VALUE + ".")
    private Integer id;

    @ApiModelProperty(
            value = "Aircraft number of plane.",
            example = "HZ4758946",
            required = true,
            position = 1)
    @Size(max = 9, message = "Max length of aircraft number of Plane entity is 9 chars.")
    @NotBlank(message = "Aircraft number of Plane entity must be at least 1 symbol")
    private String aircraftNumber;

    @ApiModelProperty(
            value = "Properties of Plane",
            example = "BuisnessJet",
            required = true,
            position = 2)
    @Size(max = 40, message = "Max length of Plane`s properties is 150 chars.")
    @NotBlank(message = "Properties of Plane must be at least 1 symbol")
    private String properties;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of Plane entity can`t be NULL.")
    private boolean disabled;

}
