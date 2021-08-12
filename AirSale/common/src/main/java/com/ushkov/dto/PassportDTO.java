package com.ushkov.dto;

import java.sql.Timestamp;

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
@ApiModel(description = "Entity, that represent Passport.")
public class PassportDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Passport in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Passport entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Passport entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of Passport entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of PassengerPassport entity can be " + Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "Firstname in Passport in latin",
            example = "Ivan",
            required = true,
            position = 1)
    @Size(max = 100, message = "Max length of firstname in Passport is 100 chars.")
    @NotBlank(message = "Firstname in Passport must be at least 1 symbol")
    private String firstNameLatin;

    @ApiModelProperty(
            value = "Lastname in Passport in latin",
            example = "Ivan",
            required = true,
            position = 2)
    @Size(max = 100, message = "Max length of lastname in Passport is 100 chars.")
    @NotBlank(message = "Lastname in Passport must be at least 1 symbol")
    private String lastNameLatin;

    @ApiModelProperty(
            value = "Series of Passport.",
            example = "HB4758946",
            required = true,
            position = 3)
    @Size(max = 12, message = "Max length of series of Passport is 12 chars.")
    @NotBlank(message = "Series of Passport must be at least 1 symbol")
    private String series;

    @ApiModelProperty(
            value = "Date of expire of Passport in TIMESTAMP format.",
            required = true,
            position = 4)
    @NotNull(message = "Date of expire of Passport can`t be NULL.")
    private Timestamp expireDate;

    @ApiModelProperty(
            value = "ID of Country entity of passport citizenship.",
            required = true,
            position = 5)
    @NotNull(message = "ID of Country entity in Passport entity can`t be NULL.")
    @Min(value = 1, message = "ID of Country entity in Passport entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Country entity in Passport entity can be " + Short.MAX_VALUE + ".")
    private Short citizenship;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 6)
    @NotNull(message = "Disabled flag of Passport entity can`t be NULL.")
    private boolean disabled;

}
