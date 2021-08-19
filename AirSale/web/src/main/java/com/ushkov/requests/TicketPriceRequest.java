package com.ushkov.requests;

import javax.validation.Valid;
import javax.validation.constraints.Min;

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
@ApiModel(description = "Entity for calculating final price.")
public class TicketPriceRequest {
    @Valid
    @Min(1)
    @ApiModelProperty(
            value = "ID of CurrentFlight entity.",
            required = true,
            position = 0)
    private Long currentFlight;
    @Valid
    @Min(1)
    @ApiModelProperty(
            value = "ID of Discount entity.",
            required = true,
            position = 3)
    private short discount;
}
