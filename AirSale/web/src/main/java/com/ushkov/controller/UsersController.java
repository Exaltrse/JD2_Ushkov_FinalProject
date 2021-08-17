package com.ushkov.controller;

import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.Users;
import com.ushkov.dto.UsersDTO;
import com.ushkov.exception.NoPermissionForThisOperationException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.UsersMapper;
import com.ushkov.repository.springdata.RoleRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.security.util.TokenUtils;
import com.ushkov.utils.SystemRoles;
import com.ushkov.validation.ValidationGroup;

@Api(tags = "Users", value="The Users API", description = "The Users API")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class UsersController {

    private final UsersRepositorySD repository;
    private final RoleRepositorySD roleRepository;
    private final UsersMapper mapper;
    private final TokenUtils tokenUtils;

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disabled Users`s entries from DB.",
            notes = "Find all not disabled Users`s entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response= UsersDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<UsersDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value="Find User`s entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = UsersDTO.class)
    })
    @GetMapping("/id")
    public UsersDTO findOne(
            @Valid
            @Min(1)
            @Max(Integer.MAX_VALUE)
            @ApiParam(
                    value = "Id of Users entry.",
                    required = true
            )
            @RequestParam("id")
                    int id,
            @RequestHeader("X-Auth-Token") String token
            ) {
        Users user = repository.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)&&user.getId()!=id) throw new NoPermissionForThisOperationException();
        return mapper.map(
                repository
                        .findById(id)
                        .orElseThrow(
                                ()-> new NoSuchEntityException(id, Users.class.getSimpleName())));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<UsersDTO> findAll(Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyloginpart")
    public Page<UsersDTO> findByLoginpart(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "login",
                    value = "String for searching by login.",
                    required = true)
            @RequestParam
                    String login,
            Pageable page) {
        return repository.findAllByLoginIsContainingAndDisabledIsFalse(login, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find user by full login.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbynamedistinct")
    public UsersDTO findByLoginDistinct(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "login",
                    value = "String for searching by login.",
                    required = true)
            @RequestParam
            String login){
        return mapper.map(repository.findByLogin(login));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by Roles.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyroles")
    public Page<UsersDTO> findAllByRole(
            @Valid
            @Min(1)
            @Max(Short.MAX_VALUE)
            @ApiParam(
                    name = "role",
                    value = "ID of role.",
                    required = true)
            @RequestParam
                    short roleId,
            Pageable page) {
        return repository
                .findAllByRole(
                        roleRepository
                                .findById(roleId)
                                .orElseThrow(()->new NoSuchEntityException(roleId, "Role")), page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of Users`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<UsersDTO> saveAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "entities",
                    value = "List of Users`s entities for update",
                    required = true)
            @RequestBody List<UsersDTO> entities) {
        entities.forEach(e->e.setId(null));
        return repository
                .saveAll(
                    entities
                        .stream()
                        .map(mapper::map)
                        .collect(Collectors.toList()))
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Save one Users`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public UsersDTO saveOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody UsersDTO entity) {
        entity.setId(null);
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(  value = "Update Users`s entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = UsersDTO.class)
    })
    @PatchMapping()
    @Validated(ValidationGroup.ExistingObject.class)
    public UsersDTO updateOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody UsersDTO dto,
            @RequestHeader("X-Auth-Token") String token) {
        Users user = repository.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER) && user.getId() != dto.getId()) throw new NoPermissionForThisOperationException();
        return mapper
                .map(repository.saveAndFlush(mapper.map(dto)));
    }

    @PreAuthorize(SecuredRoles.ALL)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping()
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @Valid
            @Positive
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody int id,
            @RequestHeader("X-Auth-Token") String token){
        Users user = repository.findById(tokenUtils.getIdFromToken(token)).orElseThrow(NoSuchEntityException::new);
        if(user.getRole().getName().equals(SystemRoles.USER)&&user.getId()!=id) throw new NoPermissionForThisOperationException();
        repository.findById(id).orElseThrow(()-> new NoSuchEntityException(id));
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Integer> idList){

        repository.disableEntities(idList);
    }
}
