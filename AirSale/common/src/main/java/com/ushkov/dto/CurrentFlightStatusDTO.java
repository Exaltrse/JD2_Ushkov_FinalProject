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
@ApiModel(description = "Entity, that represent Current FLight Status.")
public class CurrentFlightStatusDTO {

    @ApiModelProperty(
            value = "Primary key. ID of CurrentFLightStatus in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new CurrentFLightStatus entity must be NULL.")
    @NotNull(
            groups = ExistingObject.class,
            message = "Field ID in existing CurrentFLightStatus entity must NOT be NULL.")
    @Min(
            value = 1,
            groups = ExistingObject.class,
            message = "ID of CurrentFLightStatus entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of CurrentFLightStatus entity can be " + Short.MAX_VALUE + ".")
    private Short id;

    @ApiModelProperty(
            value = "Full name of CurrentFLightStatus.",
            example = "On_Air",
            required = true,
            position = 1)
    @Size(max = 20, message = "Max length of CurrentFLightStatus is 20 chars.")
    @NotBlank(message = "CurrentFLightStatus must be at least 1 symbol.")
    private String name;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 2)
    @NotNull(message = "Disabled flag of CurrentFLightStatus entity can`t be NULL.")
    private boolean disabled;

}
