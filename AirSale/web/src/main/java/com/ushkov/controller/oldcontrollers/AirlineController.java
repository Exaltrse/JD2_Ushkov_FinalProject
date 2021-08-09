//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.Airline;
//import com.ushkov.repository.imlp.AirlineRepository;
//import com.ushkov.repository.springdata.AirlineRepositorySD;
//import com.ushkov.utils.OldController;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiImplicitParam;
//import io.swagger.annotations.ApiImplicitParams;
//import io.swagger.annotations.ApiOperation;
//import io.swagger.annotations.ApiParam;
//import io.swagger.annotations.ApiResponse;
//import io.swagger.annotations.ApiResponses;
//import lombok.RequiredArgsConstructor;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@OldController
//@Api(tags = "Airline", value="The Airline API", description = "The Airline API")
//@RestController
//@RequestMapping("/airline")
//@RequiredArgsConstructor
//public class AirlineController {
//
//    private final AirlineRepository airlineRepository;
//
//    private final AirlineRepositorySD airlineRepositorySDI;
//
//    @ApiOperation(  value = "Find all Airlines entries from DB.",
//                    notes = "Find all Airlines entries from DB.",
//                    httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response=Airline.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<Airline> findAll() {
//        return airlineRepository.findAll();
//    }
//
//    @ApiOperation(  value="Find Airline entry from DB by ID.",
//                    notes = "Use ID param of entity for searching of entry in DB.",
//                    httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of airline entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = Airline.class)
//    })
//    @GetMapping("/id")
//    public Airline findOne(@RequestParam("id") Short id) {
//        return airlineRepository.findOne(id);
//    }
//
//    @ApiOperation(  value = "Find [limit] entries from DB with [offset].",
//                    httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "limit",
//                    dataType = "string",
//                    paramType = "query",
//                    value = "Limit entries in result list",
//                    required = true),
//            @ApiImplicitParam(
//                    name = "offset",
//                    dataType = "string",
//                    paramType = "query",
//                    value = "Offset from the beginning of results.",
//                    required = true),
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entries found successfully.",
//                    response = Airline.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<Airline> findLimitOffset(@RequestParam("limit") Short limit, @RequestParam("offset") Short offset) {
//        return airlineRepository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of Airline`s entities to DB",
//                    httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = Airline.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<Airline> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of Airline`s entities for update",
//                    required = true)
//            @RequestBody List<Airline> entities) {
//        return airlineRepository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one Airline`s entity to DB",
//                    httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = Airline.class)
//    })
//    @PostMapping("/post")
//    public Airline saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody Airline entity) {
//        return airlineRepository.saveOne(entity);
//
//    }
//
//    @ApiOperation(  value = "Update Airline`s entity in DB.",
//                    httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = Airline.class)
//    })
//    @PutMapping("/put")
//    public Airline updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody Airline entity) {
//        return airlineRepository.updateOne(entity);
//    }
//}
