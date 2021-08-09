//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.AirlinePlane;
//import com.ushkov.repository.imlp.AirlinePlaneRepository;
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
//@Api(tags = "Airline-Plane", value="The Airline-Plane API")
//@RestController
//@RequestMapping("/airline-plane")
//@RequiredArgsConstructor
//public class AirlinePlaneController {
//
//    private final AirlinePlaneRepository airlinePlaneRepository;
//
//    @ApiOperation(  value = "Find all AirlinePlane`s entries from DB.",
//            notes = "Find all AirlinePlane`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = AirlinePlane.class,
//                    responseContainer = "List")
//    })
//    @GetMapping
//    public List<AirlinePlane> findAll() {
//        return airlinePlaneRepository.findAll();
//    }
//
//    @ApiOperation(  value="Find AirlinePlane entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of airline/plane entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = AirlinePlane.class)
//    })
//    @GetMapping("/id")
//    public AirlinePlane findOne(@RequestParam("id") Long id) {
//        return airlinePlaneRepository.findOne(id);
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
//                    response = AirlinePlane.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<AirlinePlane> findLimitOffset(@RequestParam("limit") Long limit,
//                                              @RequestParam("offset") Long offset) {
//        return airlinePlaneRepository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of AirlinePlane`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = AirlinePlane.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<AirlinePlane> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of AirlinePlane`s entities for update",
//                    required = true)
//            @RequestBody List<AirlinePlane> entities) {
//        return airlinePlaneRepository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one AirlinePlane`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = AirlinePlane.class)
//    })
//    @PostMapping("/post")
//    public AirlinePlane saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody AirlinePlane entity) {
//        return airlinePlaneRepository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update AirlinePlane`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = AirlinePlane.class)
//    })
//    @PutMapping("/put")
//    public AirlinePlane updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody AirlinePlane entity) {
//        return airlinePlaneRepository.updateOne(entity);
//    }
//}
