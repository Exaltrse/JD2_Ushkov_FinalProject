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

import com.ushkov.domain.PassengerPassport;
import com.ushkov.domain.Passport;
import com.ushkov.dto.PassengerDTO;
import com.ushkov.dto.PassportDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.PassportMapper;
import com.ushkov.repository.springdata.PassengerPassportRepositorySD;
import com.ushkov.repository.springdata.PassengerRepositorySD;
import com.ushkov.repository.springdata.PassportRepositorySD;
import com.ushkov.security.util.SecuredRoles;

@Api(tags = "Passport", value="The Passport API", description = "The Passport API")
@RestController
@RequestMapping("/passport")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class PassportController {

    private final PassportRepositorySD repository;
    private final PassengerRepositorySD passengerRepositorySD;
    private final PassengerPassportRepositorySD passengerPassportRepositorySD;
    private final PassportMapper mapper;

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(  value = "Find all not disabled Passports entries from DB.",
            notes = "Find all not disabled Passports entries from DB.",
            httpMethod = "GET")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=PassportDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<PassportDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
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
                    response = PassportDTO.class)
    })
    @GetMapping("/id")
    public PassportDTO findOne(@RequestParam("id") long id) {

        return mapper.map(repository.findById(id)
                .orElseThrow(()-> new NoSuchEntityException(id, Passport.class.getSimpleName())));
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
    public Page<PassportDTO> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyname")
    public Page<PassportDTO> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByFirstNameLatinIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by lastname or part of lastname.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbylastname")
    public Page<PassportDTO> findByLastname(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @RequestParam
                    String name,
            Pageable page) {
        return repository.findAllByLastNameLatinIsContainingAndDisabledIsFalse(name, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find not disables entities by firstname and lastname or part of it.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyfirstandlastname")
    public Page<PassportDTO> findByFirstAndLastname(
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
                .findByFirstNameLatinIsContainingAndLastNameLatinIsContainingAndDisabledIsFalse(firstName, lastName, page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find passports by passenger.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @GetMapping("/findbypassenger")
    public Page<PassportDTO> findAllPassportsByPassenger(
            @ApiParam(
                    name = "passenger",
                    value = "Passenger entity to search for dependent passport entities.",
                    required = true)
            @RequestParam
                    PassengerDTO passenger,
            Pageable page){
        passengerRepositorySD.findById(passenger.getId()).orElseThrow(()->new NoSuchEntityException(passenger.getId(), "Passenger"));
        List<PassengerPassport> passengerPassportList = passengerPassportRepositorySD.findAllByPassenger(passenger.getId());
        if(passengerPassportList.isEmpty()) throw new NoSuchEntityException("There is no passports that depended of passenger ID " + passenger.getId() + ".");
        List<Long> idList = passengerPassportList.stream().map(PassengerPassport::getPassport).distinct().collect(Collectors.toList());
        return repository.findAllByIdInAndDisabledIsFalse(idList, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ALLEXCEPTUSER)
    @ApiOperation(value = "Find passports by it series.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @GetMapping("/findbyseries")
    public Page<PassportDTO> findAllPassportsBySeries(
            @ApiParam(
                    name = "series",
                    value = "Series of passport or it part. String.",
                    required = true)
            @RequestParam
                    String series,
            Pageable page){
        return repository.findAllBySeriesContainingAndDisabledIsFalse(series, page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
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
    public List<PassportDTO> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of Passport`s entities for update",
                    required = true)
            @RequestBody List<PassportDTO> entities) {
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one Passport`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public PassportDTO saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody PassportDTO entity) {
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update Passport`s entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = PassportDTO.class)
    })
    @PatchMapping()
    public PassportDTO updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody PassportDTO entity) {
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    //TODO: Check for ROLE_USER and allow to get information only for it ID.
    @DeleteMapping()
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody long id){
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableAll(
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Long> idList){
        repository.disableEntities(idList);
    }
}
