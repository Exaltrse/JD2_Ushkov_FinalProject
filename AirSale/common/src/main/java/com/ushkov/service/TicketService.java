package com.ushkov.service;

import java.math.BigDecimal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.ushkov.domain.CurrentFlight;
import com.ushkov.domain.Discount;
import com.ushkov.mapper.CurrentFlightMapper;
import com.ushkov.mapper.DiscountMapper;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final CurrentFlightMapper currentFlightMapper;
    private final DiscountMapper discountMapper;

    public BigDecimal getPrice(long currentFlight, short discount) {
        //Primitive calculating of final price.
        Discount discountLocal = discountMapper.mapFromId(discount);
        CurrentFlight currentFlightLocal = currentFlightMapper.mapFromId(currentFlight);
        return currentFlightLocal
                .getBasePrice()
                .subtract(
                        currentFlightLocal
                                .getBasePrice()
                                .multiply(BigDecimal.valueOf(discountLocal.getValue()/100)));
    }
}
