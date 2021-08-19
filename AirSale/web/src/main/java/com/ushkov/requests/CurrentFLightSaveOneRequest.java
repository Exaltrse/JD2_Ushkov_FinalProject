package com.ushkov.requests;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import com.ushkov.dto.CurrentFlightDTO;

@Data
@NoArgsConstructor
@EqualsAndHashCode
@Validated
@ApiModel(description = "")
public class CurrentFLightSaveOneRequest {
    private CurrentFlightDTO entity;

    private String timezoneid;
}
