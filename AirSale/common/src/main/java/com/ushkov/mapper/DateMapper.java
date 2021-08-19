package com.ushkov.mapper;

import java.sql.Date;
import java.sql.Timestamp;

import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Component
@Mapper(componentModel = "spring")
public interface DateMapper {
    default Date doMap(Timestamp timestamp){
        return timestamp.toInstant() == null ? null : (Date) Date.from(timestamp.toInstant());
    }

    default Timestamp doMap(Date date){
        return date.toInstant() == null ? null : Timestamp.from(date.toInstant());
    }
}
