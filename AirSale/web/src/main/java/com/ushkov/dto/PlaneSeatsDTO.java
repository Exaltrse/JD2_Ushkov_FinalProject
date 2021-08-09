package com.ushkov.dto;

import com.ushkov.domain.SeatClass;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlaneSeatsDTO {

    private Integer plane;

    private SeatClass seatClass;

    private Short numberOfSeats;
}
