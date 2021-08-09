//package com.ushkov.controller.oldcontrollers;
//
//
//import com.ushkov.domain.PlaneSeats;
//import com.ushkov.repository.imlp.PlaneSeatsRepository;
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
//@Api(tags = "PlaneSeats", value="The PlaneSeats API")
//@RestController
//@RequestMapping("/planeseats")
//@RequiredArgsConstructor
//public class PlaneSeatsController {
//
//    private final PlaneSeatsRepository repository;
//
//    @ApiOperation(  value = "Find all PlaneSeats`s entries from DB.",
//            httpMethod = "GET")
//    @ApiResponses(value = {
//            @ApiResponse(
//                    code = 200,
//                    message = "Success.",
//                    response = PlaneSeats.class,
//                    responseContainer="List")
//    })
//    @GetMapping
//    public List<PlaneSeats> findAll() {
//        return repository.findAll();
//    }
//
//    @ApiOperation(  value="Find PlaneSeats`s entry from DB by ID.",
//            notes = "Use ID param of entity for searching of entry in DB.",
//            httpMethod="GET")
//    @ApiImplicitParams({
//            @ApiImplicitParam(
//                    name = "id",
//                    value = "Id of PlaneSeats`s entry.",
//                    required = true,
//                    dataType = "string",
//                    paramType = "query")
//    })
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entry found successfully.",
//                    response = PlaneSeats.class)
//    })
//    @GetMapping("/id")
//    public PlaneSeats findOne(@RequestParam("id") Integer id) {
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
//                    response = PlaneSeats.class,
//                    responseContainer = "List")
//    })
//    @GetMapping("/limitoffset")
//    public List<PlaneSeats> findLimitOffset(@RequestParam("limit") Integer limit,
//                                            @RequestParam("offset") Integer offset) {
//        return repository.findLimitOffset(limit, offset);
//    }
//
//    @ApiOperation(  value = "Save list of PlaneSeats`s entities to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities saved successfully.",
//                    response = PlaneSeats.class,
//                    responseContainer = "List")
//    })
//    @PostMapping("/postall")
//    public List<PlaneSeats> saveAll(
//            @ApiParam(
//                    name = "entities",
//                    value = "List of PlaneSeats`s entities for update",
//                    required = true)
//            @RequestBody List<PlaneSeats> entities) {
//        return repository.saveAll(entities);
//    }
//
//    @ApiOperation(  value = "Save one PlaneSeats`s entity to DB",
//            httpMethod = "POST")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entity saved successfully.",
//                    response = PlaneSeats.class)
//    })
//    @PostMapping("/post")
//    public PlaneSeats saveOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for save",
//                    required = true)
//            @RequestBody PlaneSeats entity) {
//        return repository.saveOne(entity);
//    }
//
//    @ApiOperation(  value = "Update PlaneSeats`s entity in DB.",
//            httpMethod = "PUT")
//    @ApiResponses({
//            @ApiResponse(
//                    code = 200,
//                    message = "Entities updated successfully.",
//                    response = PlaneSeats.class)
//    })
//    @PutMapping("/put")
//    public PlaneSeats updateOne(
//            @ApiParam(
//                    name = "entity",
//                    value = "Entity for update",
//                    required = true)
//            @RequestBody PlaneSeats entity) {
//        return repository.updateOne(entity);
//    }
//}
