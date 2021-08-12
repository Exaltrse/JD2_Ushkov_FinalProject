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
@ApiModel(description = "Entity, that represent Users.")
public class UsersDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Users in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Users entity must be NULL.")
    @NotNull(groups = NewObject.class, message = "Field ID in existing Users entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of existing Users entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Users entity can be " + Integer.MAX_VALUE + ".")
    private Integer id;

    @ApiModelProperty(
            value = "ID of user`s Role entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Role entity in Users entity can`t be NULL.")
    @Min(value = 1, message = "ID of Role entity in User entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Role entity in User entity can be " + Short.MAX_VALUE + ".")
    private Short role;

    @ApiModelProperty(
            value = "Login of User.",
            example = "Ivanov016",
            required = true,
            position = 2)
    @Size(min = 6, max = 40, message = "Login length of Users entity must be between 6 and 40 chars.")
    @NotBlank(message = "Login of Users entity must be at least 6 symbol")
    private String login;

    @ApiModelProperty(
            value = "Password of User.",
            example = "Ivad!nov016",
            required = true,
            position = 3)
    @Size(min = 6, max = 140, message = "Password length of Users entity must be between 6 and 40 chars.")
    @NotBlank(message = "Password of Users entity must be at least 140 symbol")
    private String password;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 3)
    @NotNull(message = "Disabled flag of Users entity can`t be NULL.")
    private boolean disabled;

}
