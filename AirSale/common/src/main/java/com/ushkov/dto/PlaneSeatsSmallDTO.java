package com.ushkov.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;



@Data
@NoArgsConstructor
@EqualsAndHashCode
@Validated
@ApiModel(description = "Short entity, that represent PlaneSeats. Seats, with number of it for each class represented in plane.")
public class PlaneSeatsSmallDTO {
    @ApiModelProperty(
            value = "ID of Plane entity.",
            required = true,
            position = 0)
    @NotNull(message = "ID of Plane entity in PlaneSeats entity can`t be NULL.")
    @Min(value = 1, message = "ID of Plane entity in PlaneSeats entity must be positive number.")
    private Integer plane;
    @ApiModelProperty(
            value = "SeatClassDTO entity.",
            required = true,
            position = 1)
    private SeatClassDTO seatClass;
    @ApiModelProperty(
            value = "Number of seats.",
            example = "20",
            required = true,
            position = 2)
    @NotNull(message = "Number of seats in PlaneSeats entity can`t be NULL.")
    @Min(value = 0, message = "Number of seats in PlaneSeats must be positive number or 0.")
    private Short numberOfSeats;
}
