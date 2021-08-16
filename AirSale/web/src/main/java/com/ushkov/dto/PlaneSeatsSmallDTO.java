package com.ushkov.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.ushkov.domain.SeatClass;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaneSeatsSmallDTO {

    private Integer plane;

    private SeatClass seatClass;

    private Short numberOfSeats;
}
