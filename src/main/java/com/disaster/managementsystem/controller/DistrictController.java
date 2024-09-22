package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.DistrictDomain;
import com.disaster.managementsystem.dto.DistrictDto;
import com.disaster.managementsystem.entity.District;
import com.disaster.managementsystem.param.DistrictParam;
import com.disaster.managementsystem.param.PageableParam;
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
import jakarta.validation.Valid;
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
@RequestMapping(ApiProvider.District.ROOTPATH)
public class DistrictController extends AbstractController implements GetApi<DistrictDto>, GetAllApi<District>, CreateApi<DistrictDto, DistrictParam>, UpdateApi<DistrictDto, DistrictParam>, DeleteApi {
    private final DistrictDomain districtDomain;

    @Operation(summary = "Create")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DistrictDto.class)))})
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResponseDto<DistrictDto>> save(@RequestBody @Valid DistrictParam param) throws Exception {
        log.debug("Request to Create job-post... {}", param);
        // Return result and message.
        return generateResponse(districtDomain.create(param), HttpStatus.CREATED, "Data created successfully");
    }

    @Operation(summary = "find data by id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DistrictDto.class)))})
    @GetMapping(value = ApiProvider.District.DISTRICT_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<DistrictDto>> findById(@PathVariable("id") UUID id) {
        Optional<DistrictDto> jobPostOptional = districtDomain.getById(id);
        return generateResponse(jobPostOptional, HttpStatus.OK, "Data found");
    }

    @Operation(summary = "get all data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DistrictDto.class)))})
    @Parameters({@Parameter(in = ParameterIn.QUERY, name = "geoCode", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "latitude", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "longitude", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")), @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")), @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")), @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")), @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")})
    @GetMapping
    @Override
    public ResponseEntity findAll(@And({@Spec(path = "longitude", params = "longitude", spec = LikeIgnoreCase.class), @Spec(path = "latitude", params = "latitude", spec = LikeIgnoreCase.class), @Spec(path = "geoCode", params = "geoCode", spec = LikeIgnoreCase.class), @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class), @Spec(path = "active", params = "active", spec = Equal.class),}) @Schema(hidden = true) Specification<District> specification, @Schema(hidden = true) PageableParam pageable) {

        if (pageable.isPageable()) {
            Page<DistrictDto> jobPostDtos = districtDomain.getAll(specification, pageable.getPageable());
            return generateResponse(jobPostDtos, HttpStatus.OK, "Data found");
        }
        return generateResponse(districtDomain.getAll(specification, pageable.getSort()), HttpStatus.OK, "Data found");
    }

    @Operation(summary = "update data")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DistrictDto.class)))})
    @PutMapping(value = ApiProvider.District.DISTRICT_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<DistrictDto>> update(@PathVariable("id") UUID id, @RequestBody DistrictParam param) throws Exception {
        log.debug("Request to Update job-post...{} --> {}", id, param);
        param.setId(id);
        Optional<DistrictDto> jobPostDtoOptional = districtDomain.update(param);
        return generateResponse(jobPostDtoOptional, HttpStatus.OK, "Data updated successfully");
    }

    @Operation(summary = "delete")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}), @ApiResponse(responseCode = "404", description = "Data not found", content = @Content)})
    @DeleteMapping(value = ApiProvider.District.DISTRICT_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        districtDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Data deleted successfully");
    }

}
