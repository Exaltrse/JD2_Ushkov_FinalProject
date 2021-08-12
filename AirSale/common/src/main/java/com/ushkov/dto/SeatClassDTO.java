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
@ApiModel(description = "Entity, that represent SeatClass.")
public class SeatClassDTO {

    @ApiModelProperty(
            value = "Primary key. ID of SeatClass in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new SeatClass entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing SeatClass entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of existing SeatClass entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of SeatClass entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Name of SeatClass",
            example = "1 class",
            required = true,
            position = 1)
    @Size(max = 30, message = "Max length of SeatClass name is 30 chars.")
    @NotBlank(message = "Name of SeatClass must be at least 1 symbol")
    private String name;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of SeatClass entity can`t be NULL.")
    private boolean disabled;

}
