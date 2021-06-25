package com.ushkov.controller;


import com.ushkov.domain.Airline;
import com.ushkov.repository.imlp.AirlineRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/airline")
@RequiredArgsConstructor
public class AirlineController {
    //TODO: Write other annotations fore Swagger

    private final AirlineRepository airlineRepository;

    @ApiOperation(value = "Find all Airlines entries from DB.")
    @GetMapping
    public List<Airline> findAll() {
        return airlineRepository.findAll();
    }

    @ApiOperation(value = "Find all Airlines entries from DB.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Users was successfully created!"),
            @ApiResponse(code = 500, message = "Internal server error!")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", dataType = "integer", paramType = "query", value = "Id of airline entry.")
    })
    @GetMapping
    public Airline findOne(@RequestParam("id") Short id) {
        return airlineRepository.findOne(id);
    }
    @ApiOperation(value = "Find [limit] entries from DB with [offset].")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limit", dataType = "integer", paramType = "query", value = "Limit entries in result list"),
            @ApiImplicitParam(name = "offset", dataType = "integer", paramType = "query", value = "Offset from the beginning of results."),
    })
    @GetMapping
    public List<Airline> findLimitOffset(@RequestParam("limit") Short limit, @RequestParam("offset") Short offset) {
        return airlineRepository.findLimitOffset(limit, offset);
    }

    @ApiOperation(value = "Save list of Airline`s entities to DB")
    @PostMapping("/postall")
    public List<Airline> saveAll(List<Airline> entities) {
        return airlineRepository.saveAll(entities);
    }
    @ApiOperation(value = "Save one Airline`s entity to DB")
    @PostMapping("/post")
    public Airline saveOne(Airline entity) {
        return airlineRepository.saveOne(entity);
    }
    @ApiOperation(value = "Update Airline`s entity in DB.")
    @PutMapping("/put")
    public Airline updateOne(Airline entity) {
        return airlineRepository.updateOne(entity);
    }
}
