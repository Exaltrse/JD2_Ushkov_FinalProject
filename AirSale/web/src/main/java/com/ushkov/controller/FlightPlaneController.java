package com.ushkov.controller;


import java.sql.SQLException;
import java.util.List;

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

import com.ushkov.domain.Flight;
import com.ushkov.domain.FlightPlane;
import com.ushkov.domain.Plane;
import com.ushkov.exception.ExistingEntityException;
import com.ushkov.exception.NoSuchEntityException;
import com.ushkov.repository.springdata.FlightPlaneRepositorySD;
import com.ushkov.repository.springdata.FlightRepositorySD;
import com.ushkov.repository.springdata.PlaneRepositorySD;

@Api(tags = "FlightPlane", value="The FlightPlane API", description = "The FlightPlane API")
@RestController
@RequestMapping("/flightplane")
@RequiredArgsConstructor
public class FlightPlaneController {

    private final FlightPlaneRepositorySD repository;
    private final PlaneRepositorySD planeRepositorySD;
    private final FlightRepositorySD flightRepositorySD;

    @ApiOperation(  value = "Find all not disabled FlightPlanes entries from DB.",
            notes = "Find all not disabled FlightPlanes entries from DB.",
            httpMethod = "GET")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success.",
                    response=FlightPlane.class,
                    responseContainer="List")
    })
    @GetMapping
    public List<FlightPlane> findAll() {

        return repository.findAllByDisabledIsFalse();
    }

    @ApiOperation(  value="Find FlightPlane entry from DB by ID.",
            notes = "Use ID param of entity for searching of entry in DB. Also search in disabled entities.",
            httpMethod="GET")
    @ApiImplicitParams({
            @ApiImplicitParam(
                    name = "id",
                    value = "Id of FlightPlane entry.",
                    required = true,
                    dataType = "string",
                    paramType = "query")
    })
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entry found successfully.",
                    response = FlightPlane.class)
    })
    @GetMapping("/id")
    public FlightPlane findOne(@RequestParam("id") int id) {

        return repository.findById(id).orElseThrow(()-> new NoSuchEntityException(NoSuchEntityException.Cause.NO_SUCH_ID + String.valueOf(id)));
    }

    @ApiOperation(  value = "Find not disable entries from DB by id of flight and plane.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/findbyflightandplane")
    public List<FlightPlane> findAllByFlightAndPlane(
            @ApiParam(
                    name = "flightid",
                    value = "ID of Flight.",
                    required = true)
            @RequestParam
                    int flightId,
            @ApiParam(
                    name = "planeid",
                    value = "ID of plane.",
                    required = true)
            @RequestParam
                    int planeId,
            Pageable page) {
        Flight flight = flightRepositorySD.findById(flightId).orElseThrow(()->new NoSuchEntityException(flightId, Flight.class.getSimpleName()));
        Plane plane = planeRepositorySD.findById(planeId).orElseThrow(()->new NoSuchEntityException(planeId, Plane.class.getSimpleName()));
        if(repository.existsFlightPlaneByFlightAndPlaneAndDisabledIsFalse(flightId, planeId))
            throw new ExistingEntityException(ExistingEntityException.Cause.ALREADY_EXIST);
        return repository.findByFlightAndPlaneAndDisabledIsFalse(flight, plane);
    }

    @ApiOperation(  value = "Find all not disables entries from DB with pagination.")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entries found successfully.")
    })
    @GetMapping("/page")
    public Page<FlightPlane> findAll(Pageable page) {
        return repository.findAllByDisabledIsFalse(page);
    }

    @ApiOperation(  value = "Save list of FlightPlane`s entities to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities saved successfully.")
    })
    @PostMapping("/postall")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public List<FlightPlane> saveAll(
            @ApiParam(
                    name = "entities",
                    value = "List of FlightPlane`s entities for update",
                    required = true)
            @RequestBody List<FlightPlane> entities) {
        return repository.saveAll(entities);
    }

    @ApiOperation(  value = "Save one FlightPlane`s entity to DB",
            httpMethod = "POST")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entity saved successfully.")
    })
    @PostMapping("/post")
    public FlightPlane saveOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for save",
                    required = true)
            @RequestBody FlightPlane entity) {
        return repository.save(entity);
    }

    @ApiOperation(  value = "Update FlightPlane`s entity in DB.",
            httpMethod = "PUT")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @ApiResponses({
            @ApiResponse(
                    code = 200,
                    message = "Entities updated successfully.",
                    response = FlightPlane.class)
    })
    @PutMapping("/put")
    public FlightPlane updateOne(
            @ApiParam(
                    name = "entity",
                    value = "Entity for update",
                    required = true)
            @RequestBody FlightPlane entity) {
        return repository.saveAndFlush(entity);
    }


    @ApiOperation(value = "Set flag DISABLED in entity in DB.")
    @ApiImplicitParam(name = "X-Auth-Token", value = "token", required = true, dataType = "string", paramType = "header")
    @DeleteMapping("/disable")
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, rollbackFor = SQLException.class)
    public void disableOne(
            @ApiParam(
                    name = "id",
                    value = "ID of entity for disabling.",
                    required = true)
            @RequestBody int id){
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
            @RequestBody List<Integer> idList){
        repository.disableEntities(idList);
    }
}
