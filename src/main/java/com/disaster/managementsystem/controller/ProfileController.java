package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.AbstractController;
import com.disaster.managementsystem.controller.core.CreateApi;
import com.disaster.managementsystem.controller.core.GetAllApi;
import com.disaster.managementsystem.controller.core.GetApi;
import com.disaster.managementsystem.domain.ProfileDomain;
import com.disaster.managementsystem.dto.ProfileDto;
import com.disaster.managementsystem.entity.Profile;
import com.disaster.managementsystem.param.CreateProfileByAdminParam;
import com.disaster.managementsystem.param.PageableParam;
import com.disaster.managementsystem.param.ProfileParam;
import com.disaster.managementsystem.support.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.criteria.JoinType;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import net.kaczmarzyk.spring.data.jpa.domain.Equal;
import net.kaczmarzyk.spring.data.jpa.domain.LikeIgnoreCase;
import net.kaczmarzyk.spring.data.jpa.web.annotation.And;
import net.kaczmarzyk.spring.data.jpa.web.annotation.JoinFetch;
import net.kaczmarzyk.spring.data.jpa.web.annotation.Spec;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiProvider.Profile.ROOTPATH)
public class ProfileController extends AbstractController implements GetAllApi<Profile>, GetApi<ProfileDto>, CreateApi<ProfileDto, ProfileParam>/*, UpdateApi<JobSeekerDto, JobSeekerParam>, DeleteApi*/{

    private final ProfileDomain profileDomain;


    @Operation(summary = "Apply for the job")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProfileDto.class)))
    })
    @PostMapping()
    @Override
    public ResponseEntity<ApiResponseDto<ProfileDto>> save(@Valid @RequestBody ProfileParam param) throws Exception {
        log.debug("Request to Job online application creation... {}", param);
        // Return result and message.
        return generateResponse(profileDomain.create(param), HttpStatus.CREATED, "Job Application Successful!");
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProfileDto.class)))
    })
    @PostMapping(ApiProvider.Profile.PROFILE_CREATE_BY_ADMIN)
    public ResponseEntity<ApiResponseDto<ProfileDto>> createProfileByAdmin(@Valid @RequestBody CreateProfileByAdminParam param) throws Exception {
        return generateResponse(profileDomain.createProfileByAdmin(param), HttpStatus.CREATED, "Profile creation successful!");
    }

//    @DeleteMapping(value = ApiProvider.SimecBranch.SIMEC_BRANCH_IDENTIFIER)
//    @Override
//    public ResponseEntity<DeleteResponseDto> deleteById(@PathVariable("id") UUID id) throws Exception {
//        JobSeekerDomain.delete(id);
//        return generateResponse(HttpStatus.OK, "Deleted success");
//    }

    @Override
    @Operation(summary = "get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProfileDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "name", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "userType", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "status", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "dateOfBirth", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "genderType", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "mobileNo", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping
    public ResponseEntity findAll(
            @JoinFetch(paths = "user", alias = "u", joinType = JoinType.LEFT, distinct = false)
            @And({
                    @Spec(path = "u.userType", params = "userType", spec = Equal.class),
                    @Spec(path = "name", params = "name", spec = LikeIgnoreCase.class),
                    @Spec(path = "status", params = "status", spec = Equal.class),
                    @Spec(path = "dateOfBirth", params = "dateOfBirth", spec = Equal.class),
                    @Spec(path = "genderType", params = "genderType", spec = Equal.class),
                    @Spec(path = "mobileNo", params = "mobileNo", spec = LikeIgnoreCase.class),
                    @Spec(path = "active", params = "active", defaultVal = "true", spec = Equal.class),
    }) @Schema(hidden = true) Specification<Profile> specification, @Schema(hidden = true) PageableParam pageable) {
        if (pageable.isPageable()) {
            return generateResponse(profileDomain.getAll(specification, pageable.getPageable()), HttpStatus.OK, "data fetch success!");
        }
        return generateResponse(profileDomain.getAll(specification, pageable.getSort()), HttpStatus.OK,"data fetch success!" );
    }



    @Operation(summary = "find data by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = ProfileDto.class)))
    })
    @GetMapping(value = ApiProvider.Profile.PROFILE_IDENTIFIER)
    @Override
    public ResponseEntity<ApiResponseDto<ProfileDto>> findById(@PathVariable("id") UUID id) {
        Optional<ProfileDto> jobSeekerDto = profileDomain.getById(id);
        return generateResponse(jobSeekerDto, HttpStatus.OK, "Data get Successfully!");
    }



//    @Operation(summary = "update data")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
//            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = JobSeekerDto.class)))
//    })
//    @PutMapping(value = ApiProvider.SimecBranch.SIMEC_BRANCH_IDENTIFIER)
//    @Override
//    public ResponseEntity<ApiResponseDto<JobSeekerDto>> update(@PathVariable("id") UUID id, @RequestBody JobSeekerParam param) throws Exception {
//        log.debug("Request to Update organization-designation...{} --> {}", id, param);
//        param.setId(id);
//        Optional<JobSeekerDto> organizationDesignationDto = JobSeekerDomain.update(param);
//        return generateResponse(organizationDesignationDto, HttpStatus.OK, "Update success!");
//    }
}