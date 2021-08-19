package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Users;
import com.ushkov.dto.UsersDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.UsersRepositorySD;

@Component
@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public abstract class UsersMapper {

    @Autowired
    protected UsersRepositorySD usersRepositorySD;

    @Mapping(target = "passengers", ignore = true)
    public abstract Users map(UsersDTO dto);

    @Mapping(target = "role", source = "role.id")
    public abstract UsersDTO map(Users entity);

    public Users mapFromId(Integer id){
        return usersRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Users.class.getSimpleName()));
    }
}
