package com.ushkov.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ushkov.domain.Role;
import com.ushkov.dto.RoleDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.RoleRepositorySD;

@Component
@Mapper(componentModel = "spring")
public abstract class RoleMapper {

    @Autowired
    protected RoleRepositorySD roleRepositorySD;

    @Mapping(target = "users", ignore = true)
    public abstract Role map(RoleDTO dto);

    public abstract RoleDTO map(Role entity);

    public Role map(Short id){
        return roleRepositorySD
                .findById(id)
                .orElseThrow(()->new NoSuchEntityException(id, Role.class.getSimpleName()));
    }
}
