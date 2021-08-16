package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.UserPassenger;
import com.ushkov.dto.PassengerDTO;
import com.ushkov.dto.UsersDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.PassengerMapper;
import com.ushkov.repository.springdata.PassengerRepositorySD;
import com.ushkov.repository.springdata.UserPassengerRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;
import com.ushkov.security.util.SecuredRoles;


//TODO: Put some logic for Roles


@Api(tags = "Passenger", value="The Passenger API", description = "The Passenger API")
@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class PassengerController {
    private final PassengerRepositorySD repository;
    private final UserPassengerRepositorySD userPassengerRepositorySD;
    private final UsersRepositorySD usersRepositorySD;
    private final PassengerMapper mapper;

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disabled Passengers entries from DB.",
            notes = "Find all not disabled Passengers entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response = PassengerDTO.class,
                    responseContainer = "List")
    })
    @GetMapping
    public List<PassengerDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value="Find Passenger entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Passenger entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = PassengerDTO.class)
    })
    @GetMapping("/id")
    public PassengerDTO findOne(@RequestParam("id") long id) {

        return mapper.map(repository.findById(id).orElseThrow(()-> new NoSuchEntityException(id, "Passenger")));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @GetMapping("/findbyname")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    public Page<PassengerDTO> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByFirstNameIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by lastname or part of lastname.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbylastname")
    public Page<PassengerDTO> findByLastname(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByLastNameIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by firstname and lastname or part of it.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyfirstandlastname")
    public Page<PassengerDTO> findByFirstAndLastname(
            @ApiParam(
                    name = "firstname",
                    value = "String for searching by firstname.",
                    required = true)
            @RequestParam
                    String firstName,
            @ApiParam(
                    name = "lastname",
                    value = "String for searching by lastname.",
                    required = true)
            @RequestParam
                    String lastName,
            Pageable page) {
        return repository
                .findByFirstNameIsContainingAndLastNameIsContainingAndDisabledIsFalse(firstName, lastName, page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find passengers by user.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @GetMapping("/findbyuser")
    public Page<PassengerDTO> findAllPassengersByUser(
            @ApiParam(
                    name = "user",
                    value = "User entity to search for dependent passenger entities.",
                    required = true)
            @RequestParam
                    UsersDTO user,
            Pageable page){
        usersRepositorySD.findById(user.getId()).orElseThrow(()->new NoSuchEntityException(user.getId(), "Users"));
        List<UserPassenger> userPassengerList = userPassengerRepositorySD.findAllByUser(user.getId());
        if(userPassengerList.isEmpty()) throw new NoSuchEntityException("There is no passengers that depended of user ID " + user.getId() + ".");
        List<Long> idList = userPassengerList.stream().map(UserPassenger::getPassenger).distinct().collect(Collectors.toList());
        return repository.findAllByIdInAndDisabledIsFalse(idList, page).map(mapper::map);
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
    public Page<PassengerDTO> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Save list of Passenger`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<PassengerDTO> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Passenger`s entities for update",
                    required = true)
            @RequestBody List<PassengerDTO> entities) {
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Save one Passenger`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public PassengerDTO saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PassengerDTO entity) {
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Update Passenger`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = PassengerDTO.class)
    })
    @PatchMapping()
    public PassengerDTO updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody PassengerDTO entity) {
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody long id){
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Long> idList){
        repository.disableEntities(idList);
    }
}
