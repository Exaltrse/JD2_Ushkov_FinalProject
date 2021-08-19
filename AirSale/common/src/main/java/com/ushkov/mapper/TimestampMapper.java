package com.ushkov.mapper;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.TimeZone;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface TimestampMapper {

    default ZonedDateTime doMap(Timestamp timestamp, @Context TimeZone zone) {
        return timestamp.toInstant() == null ? null : ZonedDateTime.ofInstant(timestamp.toInstant(), zone.toZoneId());
    }

    default Timestamp doMap(ZonedDateTime zonedDateTime){
        return zonedDateTime.toInstant() == null ? null : Timestamp.from(zonedDateTime.toInstant());
    }
}
