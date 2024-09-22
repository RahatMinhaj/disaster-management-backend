package com.disaster.managementsystem.controller;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.AbstractController;
import com.disaster.managementsystem.domain.UserDomain;
import com.disaster.managementsystem.dto.InventoryDto;
import com.disaster.managementsystem.dto.MeDto;
import com.disaster.managementsystem.dto.UserDto;
import com.disaster.managementsystem.entity.User;
import com.disaster.managementsystem.param.PageableParam;
import com.disaster.managementsystem.support.ApiResponseDto;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiProvider.User.ROOTPATH)
public class UserController extends AbstractController {

    private final UserDomain userDomain;

    @Operation(summary = "Me")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = MeDto.class)))
    })
    @GetMapping(ApiProvider.User.ME)
    public ResponseEntity<ApiResponseDto<MeDto>> me() {
        Optional<MeDto> meDto = userDomain.me();
        return generateResponse(meDto, HttpStatus.OK, "Data get Successfully!");
    }


    @Operation(summary = "get all data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = InventoryDto.class)))
    })
    @Parameters({
            @Parameter(in = ParameterIn.QUERY, name = "userType", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "profileCreated", schema = @Schema(type = "boolean")),
            @Parameter(in = ParameterIn.QUERY, name = "userName", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "email", schema = @Schema(type = "string")),
            @Parameter(in = ParameterIn.QUERY, name = "active", schema = @Schema(type = "boolean")),
            @Parameter(name = "pageNo", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "1")),
            @Parameter(name = "pageSize", in = ParameterIn.QUERY, schema = @Schema(type = "int", defaultValue = "20")),
            @Parameter(name = "sortBy", in = ParameterIn.QUERY, schema = @Schema(type = "string"), example = "createdAt:desc")
    })
    @GetMapping
    public ResponseEntity findAll(
            @And({
                    @Spec(path = "userType", params = "userType", spec = Equal.class),
                    @Spec(path = "profileCreated", params = "profileCreated", spec = Equal.class),
                    @Spec(path = "userName", params = "userName", spec = LikeIgnoreCase.class),
                    @Spec(path = "userName", params = "userName", spec = LikeIgnoreCase.class),

                    @Spec(path = "active", params = "active", spec = Equal.class),
            }) @Schema(hidden = true) Specification<User> specification, @Schema(hidden = true) PageableParam pageable) {

        if (pageable.isPageable()) {
            Page<UserDto> userDtos = userDomain.getAll(specification, pageable.getPageable());
            return generateResponse(userDtos, HttpStatus.OK, "Data found");
        }
        return generateResponse(userDomain.getAll(specification, pageable.getSort()), HttpStatus.OK, "Data found");
    }
}