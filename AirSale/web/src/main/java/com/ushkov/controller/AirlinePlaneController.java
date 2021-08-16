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

import com.ushkov.domain.AirlinePlane;
import com.ushkov.dto.AirlinePlaneDTO;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.mapper.AirlinePlaneMapper;
import com.ushkov.repository.springdata.AirlinePlaneRepositorySD;
import com.ushkov.repository.springdata.AirlineRepositorySD;
import com.ushkov.repository.springdata.PlaneRepositorySD;
import com.ushkov.security.util.SecuredRoles;

@Api(tags = "AirlinePlane", value="The AirlinePlane API", description = "The AirlinePlane API. For admins only.")
@RestController
@RequestMapping("/airlineplane")
@RequiredArgsConstructor
@PreAuthorize(SecuredRoles.SUPERADMIN)
@Validated
public class AirlinePlaneController {

    private final AirlinePlaneRepositorySD repository;
    private final AirlineRepositorySD airlineRepositorySD;
    private final PlaneRepositorySD planeRepositorySD;
    private final AirlinePlaneMapper mapper;

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Find all not disabled AirlinePlanes entries from DB.",
            notes = "Find all not disabled AirlinePlanes entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=AirlinePlaneDTO.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<AirlinePlaneDTO> findAll() {

        return repository.findAllByDisabledIsFalse().stream().map(mapper::map).collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value="Find AirlinePlane entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of AirlinePlane entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query"),
            @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = AirlinePlaneDTO.class)
    })
    @GetMapping("/id")
    public AirlinePlaneDTO findOne(@RequestParam("id") Long id) {

        return mapper.map(repository.findById(id)
                .orElseThrow(()-> new NoSuchEntityException(id, AirlinePlane.class.getSimpleName())));
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<AirlinePlaneDTO> findAll(Pageable page) {

        return repository.findAllByDisabledIsFalse(page).map(mapper::map);
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Find not disable entries from DB by id of airline and plane.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/findbyairlineandplane")
    public List<AirlinePlaneDTO> findAllByAirlineAndPlane(
            @ApiParam(
                    name = "airlineid",
                    value = "ID of Airlines.",
                    required = true)
            @RequestParam
            short airlineId,
            @ApiParam(
                    name = "planeid",
                    value = "ID of plane.",
                    required = true)
            @RequestParam
            int planeId) {
        return repository
                .findByAirlineAndPlaneAndDisabledIsFalse(airlineId, planeId)
                .stream()
                .map(mapper::map)
                .collect(Collectors.toList());
    }

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save list of AirlinePlane`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(
            name = "X-Auth-Token",
            value = "token",
            required = true,
            dataType = "string",
            paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<AirlinePlaneDTO> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of AirlinePlane`s entities for update",
                    required = true)
            @RequestBody List<AirlinePlaneDTO> entities) {
        entities.forEach(
                e-> {
                    airlineRepositorySD.findById(e.getAirline());
                    planeRepositorySD.findById(e.getPlane());
                });
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

    @PreAuthorize(SecuredRoles.ONLYADMINS)
    @ApiOperation(  value = "Save one AirlinePlane`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping()
    public AirlinePlaneDTO saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody AirlinePlaneDTO entity) {
        airlineRepositorySD.findById(entity.getAirline());
        planeRepositorySD.findById(entity.getPlane());
        return mapper.map(repository.save(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(  value = "Update AirlinePlane`s entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = AirlinePlaneDTO.class)
    })
    @PatchMapping()
    public AirlinePlaneDTO updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody AirlinePlaneDTO entity) {
        airlineRepositorySD.findById(entity.getAirline());
        planeRepositorySD.findById(entity.getPlane());
        return mapper.map(repository.saveAndFlush(mapper.map(entity)));
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping()
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody Long id){
        repository.findById(id).orElseThrow(()->new NoSuchEntityException(id, AirlinePlane.class.getSimpleName()));
        repository.disableEntity(id);
    }

    @PreAuthorize(SecuredRoles.SUPERADMIN)
    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @ApiImplicitParam(
            name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "listid",
                    value = "List of ID of entities for disabling.",
                    required = true
            )
            @RequestBody List<Long> idList){
        idList.forEach(
                e->repository.findById(e).orElseThrow(
                        ()->new NoSuchEntityException(e, AirlinePlane.class.getSimpleName())));
        repository.disableEntities(idList);
    }
}
