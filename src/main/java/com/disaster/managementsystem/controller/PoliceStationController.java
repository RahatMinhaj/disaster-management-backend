package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.PoliceStationDomain;
import com.disaster.managementsystem.dto.PoliceStationDto;
import com.disaster.managementsystem.entity.PoliceStation;
import com.disaster.managementsystem.param.PageableParam;
import com.disaster.managementsystem.param.PoliceStationParam;
import com.disaster.managementsystem.support.ApiResponseDto;
import com.disaster.managementsystem.support.DeleteResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiProvider.PoliceStation.ROOTPATH)
public class PoliceStationController extends AbstractController implements GetApi<PoliceStationDto>, GetAllApi<PoliceStation>, CreateApi<PoliceStationDto, PoliceStationParam>, UpdateApi<PoliceStationDto, PoliceStationParam>, DeleteApi {
    private final PoliceStationDomain PoliceStationDomain;

    @Operation(summary = "Create")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = PoliceStationDto.class)))})
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResponseDto<PoliceStationDto>> save(@RequestBody PoliceStationParam param) throws Exception {
        log.debug("Request to Create job-post... {}", param);
        // Return result and message.
        return generateResponse(PoliceStationDomain.create(param), HttpStatus.CREATED, "Data created successfully");
    }

    @Operation(summary = "find data by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = PoliceStationDto.class)))})
    @GetMapping(value = ApiProvider.PoliceStation.POLICE_STATION_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<PoliceStationDto>> findById(@PathVariable("id") UUID id) {
        Optional<PoliceStationDto> jobPostOptional = PoliceStationDomain.getById(id);
        return generateResponse(jobPostOptional, HttpStatus.OK, "Data found");
    }

    @Operation(summary = "get all data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = PoliceStationDto.class)))})
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "geoCode", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "latitude", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "longitude", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")), @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")), @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")), @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")})
    @GetMapping
    @Override
    public ResponseEntity findAll(@And({@Spec(path = "longitude", params = "longitude", spec = LikeIgnoreCase.class), @Spec(path = "latitude", params = "latitude", spec = LikeIgnoreCase.class), @Spec(path = "geoCode", params = "geoCode", spec = LikeIgnoreCase.class), @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class), @Spec(path = "active", params = "active", spec = Equal.class),}) @Schema(hidden = true) Specification<PoliceStation> specification, @Schema(hidden = true) PageableParam pageable) {

        if (pageable.isPageable()) {
            Page<PoliceStationDto> all = PoliceStationDomain.getAll(specification, pageable.getPageable());
            return generateResponse(all, HttpStatus.OK, "Data found");
        }
        return generateResponse(PoliceStationDomain.getAll(specification, pageable.getSort()), HttpStatus.OK, "Data found");
    }

    @Operation(summary = "update data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = PoliceStationDto.class)))})
    @PutMapping(value = ApiProvider.PoliceStation.POLICE_STATION_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<PoliceStationDto>> update(@PathVariable("id") UUID id, @RequestBody PoliceStationParam param) throws Exception {
        log.debug("Request to Update job-post...{} --> {}", id, param);
        param.setId(id);
        Optional<PoliceStationDto> policeStationDto = PoliceStationDomain.update(param);
        return generateResponse(policeStationDto, HttpStatus.OK, "Data updated successfully");
    }

    @Operation(summary = "delete")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content)})
    @DeleteMapping(value = ApiProvider.PoliceStation.POLICE_STATION_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        PoliceStationDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Data deleted successfully");
    }

}
