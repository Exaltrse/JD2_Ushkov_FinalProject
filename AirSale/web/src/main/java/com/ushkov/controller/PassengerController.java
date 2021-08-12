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
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ushkov.domain.Passenger;
import com.ushkov.domain.UserPassenger;
import com.ushkov.domain.Users;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassengerRepositorySD;
import com.ushkov.repository.springdata.UserPassengerRepositorySD;
import com.ushkov.repository.springdata.UsersRepositorySD;

@Api(tags = "Passenger", value="The Passenger API", description = "The Passenger API")
@RestController
@RequestMapping("/passenger")
@RequiredArgsConstructor
public class PassengerController {

    private final PassengerRepositorySD repository;

    private final UserPassengerRepositorySD userPassengerRepositorySD;

    private final UsersRepositorySD usersRepositorySD;

    @ApiOperation(  value = "Find all not disabled Passengers entries from DB.",
            notes = "Find all not disabled Passengers entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Passenger.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Passenger> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

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
                    response = Passenger.class)
    })
    @GetMapping("/id")
    public Passenger findOne(@RequestParam("id") long id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(id, "Passenger"));
    }

    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @GetMapping("/findbyname")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    public Page<Passenger> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByFirstNameIsContainingAndDisabledIsFalse(name, page);
    }

    @ApiOperation(value = "Find not disables entities by lastname or part of lastname.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbylastname")
    public Page<Passenger> findByLastname(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByLastNameIsContainingAndDisabledIsFalse(name, page);
    }

    @ApiOperation(value = "Find not disables entities by firstname and lastname or part of it.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyfirstandlastname")
    public Page<Passenger> findByFirstAndLastname(
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
        return repository.findByFirstNameIsContainingAndLastNameIsContainingAndDisabledIsFalse(firstName, lastName, page);
    }
    @ApiOperation(value = "Find passengers by user.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @GetMapping("/findbyuser")
    public Page<Passenger> findAllPassengersByUser(
            @ApiParam(
                    name = "user",
                    value = "User entity to search for dependent passenger entities.",
                    required = true)
            @RequestParam
                    Users user,
            Pageable page){
        usersRepositorySD.findById(user.getId()).orElseThrow(()->new NoSuchEntityException(user.getId(), "Users"));
        List<UserPassenger> userPassengerList = userPassengerRepositorySD.findAllByUser(user.getId());
        if(userPassengerList.isEmpty()) throw new NoSuchEntityException("There is no passengers that depended of user ID " + user.getId() + ".");
        List<Long> idList = userPassengerList.stream().map(UserPassenger::getPassenger).distinct().collect(Collectors.toList());
        return repository.findAllByIdInAndDisabledIsFalse(idList, page);
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Passenger> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

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
    public List<Passenger> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Passenger`s entities for update",
                    required = true)
            @RequestBody List<Passenger> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Passenger`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public Passenger saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Passenger entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update Passenger`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Passenger.class)
    })
    @PutMapping("/put")
    public Passenger updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Passenger entity) {
        return repository.saveAndFlush(entity);
    }

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
