package com.ushkov.requests;

import java.time.ZonedDateTime;

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
public class CurrentFlightSearchingByTimeAndStatusRequest {


    private ZonedDateTime beginingDate;

    private ZonedDateTime endDate;

    private Short currentFlightStatus;

    private String timezoneid;
}
