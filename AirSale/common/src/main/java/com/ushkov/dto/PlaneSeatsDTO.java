package com.ushkov.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

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
@ApiModel(description = "Entity, that represent PlaneSeats. Seats, with number of it for each class represented in plane.")
public class PlaneSeatsDTO {

    @ApiModelProperty(
            value = "Primary key. ID of PlaneSeats in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new PlaneSeats entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing PlaneSeats entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of existing PlaneSeats entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of PlaneSeats entity can be " + Integer.MAX_VALUE + ".")
    private Integer id;

    @ApiModelProperty(
            value = "ID of Plane entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Plane entity in PlaneSeats entity can`t be NULL.")
    @Min(value = 1, message = "ID of Plane entity in PlaneSeats entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            message = "Max value of ID of Plane entity in PlaneSeats entity can be " + Integer.MAX_VALUE + ".")
    private Integer plane;

    @ApiModelProperty(
            value = "ID of SeatClass entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of SeatClass entity in PlaneSeats entity can`t be NULL.")
    @Min(value = 1, message = "ID of SeatClass entity in PlaneSeats entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Plane entity in PlaneSeats entity can be " + Short.MAX_VALUE + ".")
    private Short seat;

    @ApiModelProperty(
            value = "Number of seats.",
            example = "20",
            required = true,
            position = 3)
    @NotNull(message = "Number of seats in PlaneSeats entity can`t be NULL.")
    @Min(value = 0, message = "Number of seats in PlaneSeats must be positive number or 0.")
    @Max(value = Short.MAX_VALUE, message = "Max number of seats in PlaneSeats entity can be " + Short.MAX_VALUE + ".")
    private Short numberOfSeats;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 4)
    @NotNull(message = "Disabled flag of PlaneSeats entity can`t be NULL.")
    private boolean disabled;

}
