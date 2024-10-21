package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.DonationFundDomain;
import com.disaster.managementsystem.dto.DonationFundDto;
import com.disaster.managementsystem.entity.DonationFund;
import com.disaster.managementsystem.param.DonationFundParam;
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
@RequestMapping(ApiProvider.DonationFund.ROOTPATH)
public class DonationFundController extends AbstractController implements GetApi<DonationFundDto>, GetAllApi<DonationFund>, CreateApi<DonationFundDto, DonationFundParam>, UpdateApi<DonationFundDto, DonationFundParam>, DeleteApi {
    private final DonationFundDomain donationFundDomain;

    @Operation(summary = "Create")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DonationFundDto.class)))
    })
    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Override
    public ResponseEntity<ApiResponseDto<DonationFundDto>> save(@RequestBody DonationFundParam param) throws Exception {
        log.debug("Request to Create educational-qualification... {}", param);
        // Return result and message.
        return generateResponse(donationFundDomain.create(param), HttpStatus.CREATED, "Data created successfully");
    }

    @Operation(summary = "find data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found",  content = @Content(schema = @Schema(implementation = DonationFundDto.class)))
    })
    @GetMapping(value = ApiProvider.DonationFund.DONATION_FUND_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<DonationFundDto>> findById(@PathVariable("id") UUID id) {
        Optional<DonationFundDto> educationalQualificationDto = donationFundDomain.getById(id);
        return generateResponse(educationalQualificationDto, HttpStatus.OK, "Data found");
    }

    @Operation(summary = "get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = DonationFundDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "educationalQualificationId", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "nameBN", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping
    @Override
    public ResponseEntity findAll(
            @And({
                    @Spec(path = "id", params = "educationalQualificationId", spec = Equal.class),
                    @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "nameBN", params = "nameBN", spec = LikeIgnoreCase.class),
                    @Spec(path = "active", params = "active", defaultVal = "true", spec = Equal.class),
            }) @Schema(hidden = true) Specification<DonationFund> specification, @Schema(hidden = true) PageableParam pageable) {
        if (pageable.isPageable()) {
            Page<DonationFundDto> educationalQualificationDtos = donationFundDomain.getAll(specification, pageable.getPageable());
            return generateResponse(educationalQualificationDtos, HttpStatus.OK, "Data found");
        }
        return generateResponse(donationFundDomain.getAll(specification, pageable.getSort()), HttpStatus.OK, "Data found");
    }

    @Operation(summary = "update data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found",  content = @Content(schema = @Schema(implementation = DonationFundDto.class)))
    })
    @PutMapping(value = ApiProvider.DonationFund.DONATION_FUND_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<DonationFundDto>> update(@PathVariable("id") UUID id, @RequestBody DonationFundParam param) throws Exception {
        log.debug("Request to Update educational-qualification...{} --> {}", id, param);
        param.setId(id);
        Optional<DonationFundDto> educationalQualificationDto = donationFundDomain.update(param);
        return generateResponse(educationalQualificationDto, HttpStatus.OK, "Data updated successfully");
    }

    @Operation(summary = "delete")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content)
    })
    @DeleteMapping(value = ApiProvider.DonationFund.DONATION_FUND_IDENTIFIER)
    @Override
    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
        donationFundDomain.delete(id);
        return generateResponse(HttpStatus.OK, "Data deleted successfully");
    }

    @Operation(summary = "get all data")
    @GetMapping(ApiProvider.DonationFund.DAY_WISE_DONATION)
    public ResponseEntity findAll() {
        return generateResponse(donationFundDomain.findLast7DayWiseDonation(), HttpStatus.OK, "Data found");
    }

}
