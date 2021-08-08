//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.Flight;
//import com.ushkov.repository.imlp.FlightRepository;
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
//@Api(tags = "Flight", value="The Flight API")
//@RestController
//@RequestMapping("/Flight")
//@RequiredArgsConstructor
//public class FlightController {
//
//    private final FlightRepository repository;
//
//    @ApiOperation(  value = "Find all Flight`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = Flight.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<Flight> findAll() {
//        return repository.findAll();
//    }
//
//    @ApiOperation(  value="Find Flight`s entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of Flight`s entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = Flight.class)
//    })
//    @GetMapping("/id")
//    public Flight findOne(@RequestParam("id") Integer id) {
//        return repository.findOne(id);
//    }
//
//    @ApiOperation(  value = "Find [limit] entries from DB with [offset].",
//            httpMethod="GET")
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
//                    response = Flight.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<Flight> findLimitOffset(@RequestParam("limit") Integer limit,
//                                        @RequestParam("offset") Integer offset) {
//        return repository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of Flight`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = Flight.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<Flight> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of Flight`s entities for update",
//                    required = true)
//            @RequestBody List<Flight> entities) {
//        return repository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one Flight`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = Flight.class)
//    })
//    @PostMapping("/post")
//    public Flight saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody Flight entity) {
//        return repository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update Flight`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = Flight.class)
//    })
//    @PutMapping("/put")
//    public Flight updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody Flight entity) {
//        return repository.updateOne(entity);
//    }
//}
