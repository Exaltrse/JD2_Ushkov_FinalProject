package com.ushkov.requests;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Validated
@ApiModel(description = "")
public class TicketCurrentFlightAndStatusRequest {

    private long currentFlightDTO;

    private short ticketStatusDTO;
}
