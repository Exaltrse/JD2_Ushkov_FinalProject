package com.ushkov.dto;

import java.math.BigDecimal;

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
@ApiModel(description = "Entity, that represent Ticket.")
public class TicketDTO {

    @ApiModelProperty(
            value = "Primary key. ID of Ticket in DB. Can be NULL only for new entities.",
            position = 0)
    @Null(groups = NewObject.class, message = "Field ID in new Ticket entity must be NULL.")
    @NotNull(groups = ExistingObject.class, message = "Field ID in existing Ticket entity must NOT be NULL.")
    @Min(value = 1, groups = ExistingObject.class, message = "ID of existing Ticket entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            groups = ExistingObject.class,
            message = "Max value of ID of Ticket entity can be " +Long.MAX_VALUE + ".")
    private Long id;

    @ApiModelProperty(
            value = "ID of Passport entity.",
            required = true,
            position = 1)
    @NotNull(message = "ID of Passport entity in Ticket entity can`t be NULL.")
    @Min(value = 1, message = "ID of Passport entity in Ticket entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            message = "Max value of ID of Passport entity in Ticket entity can be " + Long.MAX_VALUE + ".")
    private Long passport;

    @ApiModelProperty(
            value = "ID of CurrentFlight entity.",
            required = true,
            position = 2)
    @NotNull(message = "ID of CurrentFlight entity in Ticket entity can`t be NULL.")
    @Min(value = 1, message = "ID of CurrentFlight entity in Ticket entity must be positive number.")
    @Max(
            value = Long.MAX_VALUE,
            message = "Max value of ID of CurrentFlight entity in Ticket entity can be " + Long.MAX_VALUE + ".")
    private Long currentFlight;

    @ApiModelProperty(
            value = "ID of Discount entity.",
            required = true,
            position = 3)
    @NotNull(message = "ID of Discount entity in Ticket entity can`t be NULL.")
    @Min(value = 1, message = "ID of Discount entity in Ticket entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of Discount entity in Ticket entity can be " + Short.MAX_VALUE + ".")
    private Short discount;

    @ApiModelProperty(
            value = "Final Price of Ticket. Calculating value",
            position = 4)
    @Min(value = 0, message = "Final price of Ticket entity must be positive number.")
    private BigDecimal finalPrice;

    @ApiModelProperty(
            value = "ID of TicketStatus entity.",
            required = true,
            position = 5)
    @NotNull(message = "ID of TicketStatus entity in Ticket entity can`t be NULL.")
    @Min(value = 1, message = "ID of TicketStatus entity in Ticket entity must be positive number.")
    @Max(
            value = Short.MAX_VALUE,
            message = "Max value of ID of TicketStatus entity in Ticket entity can be " + Short.MAX_VALUE + ".")
    private Short ticketStatus;

    @ApiModelProperty(
            value = "ID of PlaneSeat entity.",
            required = true,
            position = 6)
    @NotNull(message = "ID of PlaneSeat entity in Ticket entity can`t be NULL.")
    @Min(value = 1, message = "ID of PlaneSeat entity in Ticket entity must be positive number.")
    @Max(
            value = Integer.MAX_VALUE,
            message = "Max value of ID of PlaneSeat entity in Ticket entity can be " + Integer.MAX_VALUE + ".")
    private Integer seat;

    @ApiModelProperty(
            value = "Represents if that entity can be used or it lost it actuality and disabled in system.",
            required = true,
            position = 7)
    @NotNull(message = "Disabled flag of Ticket entity can`t be NULL.")
    private boolean disabled;

}
