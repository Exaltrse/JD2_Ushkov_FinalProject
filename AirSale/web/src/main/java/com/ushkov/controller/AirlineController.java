package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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

import com.ushkov.domain.Airline;
import com.ushkov.dto.AirlineDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.AirlineMapper;
import com.ushkov.repository.springdata.AirlineRepositorySD;
import com.ushkov.repository.springdata.PlaneRepositorySD;
import com.ushkov.security.util.SecuredRoles;
import com.ushkov.validation.ValidationGroup;

@Api(tags = "Airline", value="The Airline API", description = "The Airline API")
@RestController
@RequestMapping("/airline")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class AirlineController {

    private final AirlineRepositorySD repository;
    private final PlaneRepositorySD planeRepositorySD;
    private final AirlineMapper mapper;

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disabled Airlines entries from DB.",
                    notes = "Find all not disabled Airlines entries from DB.",
                    httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=AirlineDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<AirlineDTO> findAll() {
        return repository
                .findAllByDisabledIsFalse()
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value="Find Airline entry from DB by ID.",
                    notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
                    httpMethod="GET")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = AirlineDTO.class)
    })
    @GetMapping("/id")
    public AirlineDTO findOne(
            @ApiParam(value = "Id of airline entry.", required = true)
            @Valid
            @Min(1)
            @Max(value = Short.MAX_VALUE, message = "Max value is " + Short.MAX_VALUE)
            @RequestParam("id")
                    Short id) {

        return mapper.map(
                repository.findById(id)
                        .orElseThrow(
                                ()-> new NoSuchEntityException(id, Airline.class.getSimpleName())));
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<AirlineDTO> findAll(Pageable page) {

        return repository
                .findAllByDisabledIsFalse(page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by name or part of name.")
    @GetMapping("/findbyname")
    public Page<AirlineDTO> findByName(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @Valid
            @NotBlank
            @RequestParam
            String name,
            Pageable page) {
        return repository
                .findAllByNameIsContainingAndDisabledIsFalse(name, page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by shortname or part of shortname.")
    @GetMapping("/findbyshortname")
    public Page<AirlineDTO> findByShortname(
            @ApiParam(
                    name = "name",
                    value = "String for searching by name.",
                    required = true)
            @Valid
            @Size(min = 1, max = 3, message = "Length of short name of airline must be between 2 and 3 chars.")
            @RequestParam
            String name,
            Pageable page) {
        return repository
                .findAllByShortNameIsContainingAndDisabledIsFalse(name, page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.WITHOUTAUTHENTICATION)
    @ApiOperation(value = "Find not disables entities by planes.")
    @GetMapping("/findbyplanes")
    public Page<AirlineDTO> findByPlanes(
            @ApiParam(
                    name = "planeidlist",
                    value = "List of ID of planes.",
                    required = true)
            @Valid
            @NotEmpty(message = "You must fill List with at least one element.")
            @Max(Integer.MAX_VALUE)
            @Min(1)
            @RequestParam
            List<Integer> planeIdList,
            Pageable page) {
        planeIdList.forEach(planeRepositorySD::findById);
        return repository
                .findAllByPlaneAndDisabledIsFalse(planeIdList, page)
                .map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of Airline`s entities to DB",
                    httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<AirlineDTO> saveAll(
            @Valid
            @NotEmpty
            @ApiParam(
                    name = "entities",
                    value = "List of Airline`s entities for update",
                    required = true)
            @RequestBody
                    List<AirlineDTO> entities) {
        entities.forEach(e->e.setId(null));
        return repository.saveAll(entities.stream().map(mapper::map).collect(Collectors.toList()))
                .stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one Airline`s entity to DB",
                    httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public AirlineDTO saveOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody
                    AirlineDTO entity) {
        entity.setId(null);
        return mapper.map(
                repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update Airline`s entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = AirlineDTO.class)
    })
    @PatchMapping()
    @Validated(ValidationGroup.ExistingObject.class)
    public AirlineDTO updateOne(
            @Valid
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody AirlineDTO entity) {
        return mapper.map(
                repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
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
            @RequestBody Short id){
            repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
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
            @RequestBody List<Short> idList){
        repository.disableEntities(idList);
    }
}
