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
import com.ushkov.domain.PassengerPassport;
import com.ushkov.domain.Passport;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.PassengerPassportRepositorySD;
import com.ushkov.repository.springdata.PassengerRepositorySD;
import com.ushkov.repository.springdata.PassportRepositorySD;

@Api(tags = "Passport", value="The Passport API", description = "The Passport API")
@RestController
@RequestMapping("/passport")
@RequiredArgsConstructor
public class PassportController {

    private final PassportRepositorySD repository;
    private final PassengerRepositorySD passengerRepositorySD;
    private final PassengerPassportRepositorySD passengerPassportRepositorySD;

    @ApiOperation(  value = "Find all not disabled Passports entries from DB.",
            notes = "Find all not disabled Passports entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=Passport.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<Passport> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find Passport entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of Passport entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = Passport.class)
    })
    @GetMapping("/id")
    public Passport findOne(@RequestParam("id") long id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + String.valueOf(id)));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<Passport> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyname")
    public Page<Passenger> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByFirstNameLatinIsContainingAndDisabledIsFalse(name, page);
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
        return repository.findAllByLastNameLatinIsContainingAndDisabledIsFalse(name, page);
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
        return repository.findByFirstNameLatinIsContainingAndLastNameLatinIsContainingAndDisabledIsFalse(firstName, lastName, page);
    }

    @ApiOperation(value = "Find passports by passenger.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @GetMapping("/findbypassenger")
    public Page<Passport> findAllPassportsByPassenger(
            @ApiParam(
                    name = "passenger",
                    value = "Passenger entity to search for dependent passport entities.",
                    required = true)
            @RequestParam
                    Passenger passenger,
            Pageable page){
        passengerRepositorySD.findById(passenger.getId()).orElseThrow(()->new NoSuchEntityException(passenger.getId(), "Passenger"));
        List<PassengerPassport> passengerPassportList = passengerPassportRepositorySD.findAllByPassenger(passenger.getId());
        if(passengerPassportList.isEmpty()) throw new NoSuchEntityException("There is no passports that depended of passenger ID " + passenger.getId() + ".");
        List<Long> idList = passengerPassportList.stream().map(PassengerPassport::getPassport).distinct().collect(Collectors.toList());
        return repository.findAllByIdInAndDisabledIsFalse(idList, page);
    }

    @ApiOperation(value = "Find passports by it series.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyseries")
    public Page<Passport> findAllPassportsBySeries(
            @ApiParam(
                    name = "series",
                    value = "Series of passport or it part. String.",
                    required = true)
            @RequestParam
                    String series,
            Pageable page){
        return repository.findAllBySeriesContainingAndDisabledIsFalse(series, page);
    }

    @ApiOperation(  value = "Save list of Passport`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<Passport> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Passport`s entities for update",
                    required = true)
            @RequestBody List<Passport> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one Passport`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public Passport saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody Passport entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update Passport`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = Passport.class)
    })
    @PutMapping("/put")
    public Passport updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody Passport entity) {
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
