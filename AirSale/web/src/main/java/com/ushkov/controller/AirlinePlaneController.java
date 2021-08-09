package com.ushkov.controller;


import com.ushkov.domain.Airline;
import com.ushkov.domain.AirlinePlane;
import com.ushkov.domain.Plane;
import com.ushkov.exception.ExistingEntityException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.AirlinePlaneRepositorySD;
import com.ushkov.repository.springdata.AirlineRepositorySD;
import com.ushkov.repository.springdata.PlaneRepositorySD;
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

import java.sql.SQLException;
import java.util.List;

@Api(tags = "AirlinePlane", value="The AirlinePlane API", description = "The AirlinePlane API")
@RestController
@RequestMapping("/airlineplane")
@RequiredArgsConstructor
public class AirlinePlaneController {

    private final AirlinePlaneRepositorySD repository;
    private final AirlineRepositorySD airlineRepositorySD;
    private final PlaneRepositorySD planeRepositorySD;

    @ApiOperation(  value = "Find all not disabled AirlinePlanes entries from DB.",
            notes = "Find all not disabled AirlinePlanes entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=AirlinePlane.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<AirlinePlane> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find AirlinePlane entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. lso search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of AirlinePlane entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = AirlinePlane.class)
    })
    @GetMapping("/id")
    public AirlinePlane findOne(@RequestParam("id") Long id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + id.toString()));
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<AirlinePlane> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(  value = "Find not disable entries from DB by id of airline and plane.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/findbyairlineandplane")
    public List<AirlinePlane> findAllByAirlineAndPlane(
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
            int planeId,
            Pageable page) {
        airlineRepositorySD.findById(airlineId).orElseThrow(()->new NoSuchEntityException(airlineId, Airline.class.getSimpleName()));
        planeRepositorySD.findById(planeId).orElseThrow(()->new NoSuchEntityException(planeId, Plane.class.getSimpleName()));
        if(repository.existsAirlinePlaneByAirlineAndPlaneAndDisabledIsFalse(airlineId, planeId))
            throw new ExistingEntityException(ExistingEntityException.Cause.ALREADY_EXIST);
        return repository.findByAirlineAndPlaneAndDisabledIsFalse(airlineId, planeId);
    }

    @ApiOperation(  value = "Save list of AirlinePlane`s entities to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<AirlinePlane> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of AirlinePlane`s entities for update",
                    required = true)
            @RequestBody List<AirlinePlane> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one AirlinePlane`s entity to DB",
            httpMethod = "POST")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public AirlinePlane saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody AirlinePlane entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update AirlinePlane`s entity in DB.",
            httpMethod = "PUT")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = AirlinePlane.class)
    })
    @PutMapping("/put")
    public AirlinePlane updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody AirlinePlane entity) {
        return repository.saveAndFlush(entity);
    }


    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(Long id){
        repository.disableEntity(id);
    }

    @ApiOperation(value = "Set flag DISABLED in entities in DB.")
    @DeleteMapping("/disableall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(List<Long> idList){
        repository.disableEntities(idList);
    }
}