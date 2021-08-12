package com.ushkov.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

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
@ApiModel(description = "Entity, that represent Current FLight.")
public class CurrentFlightDTO {
    @ApiModelProperty(
            value = "Primary key. ID of CurrentFlight in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new CurrentFlight entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing CurrentFlight entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of CurrentFlight entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of CurrentFlight entity can be " + Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "ID of Flight entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Flight entity can`t be NULL.")
    @Min(value = 1, message = "ID of Flight entity in CurrentFlight entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            message = "Max value of ID of Flight entity in CurrentFlight entity can be " + Integer.MAX_VALUE + ".")
    private Integer flight;

    @ApiModelProperty(
            value = "ID of FlightPlane entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of FlightPlane  entity in CurrentFlight entity can`t be NULL.")
    @Min(value = 1, message = "ID of FlightPlane entity in CurrentFlight entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            message = "Max value of ID of FlightPlane entity in CurrentFlight entity can be " + Long.MAX_VALUE + ".")
    private Long flightPlane;

    @ApiModelProperty(
            value = "Date and time of departure in TIMESTAMP format.",
            required = true,
            position = 3)
    @NotNull(message = "Date of departure of CurrentFlight entity can`t be NULL.")
    private Timestamp departureDate;

    @ApiModelProperty(
            value = "Date and time of arrival in TIMESTAMP format.",
            required = true,
            position = 4)
    @NotNull(message = "Date of arrival of CurrentFlight entity can`t be NULL.")
    private Timestamp arrivalDate;

    @ApiModelProperty(
            value = "Base Price of CurrentFlight. Using for calculating final price.",
            required = true,
            position = 5)
    @NotNull(message = "Base Price of CurrentFlight entity can`t be NULL.")
    @Min(value = 0, message = "Base Price of CurrentFlight entity must be positive number.")
    private BigDecimal basePrice;

    @ApiModelProperty(
            value = "ID of CurrentFlightStatus entity.",
            required = true,
            position = 6)
    @NotNull(message = "ID of CurrentFlightStatus entity can`t be NULL.")
    @Min(value = 1, message = "ID of CurrentFlightStatus entity in CurrentFlight entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of CurrentFlightStatus entity in CurrentFlight entity can be " + Short.MAX_VALUE + ".")
    private Short currentFlightsStatus;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 7)
    @NotNull(message = "Disabled flag of CurrentFlight entity can`t be NULL.")
    private boolean disabled;




}
