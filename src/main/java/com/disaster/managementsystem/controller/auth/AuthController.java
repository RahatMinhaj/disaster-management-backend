package com.disaster.managementsystem.controller.auth;

import com.disaster.managementsystem.component.route.ApiProvider;
import com.disaster.managementsystem.controller.core.*;
import com.disaster.managementsystem.domain.AuthDomain;
import com.disaster.managementsystem.dto.UserDto;
import com.disaster.managementsystem.dto.auth.JwtResponse;
import com.disaster.managementsystem.param.LoginParam;
import com.disaster.managementsystem.param.UserParam;
import com.disaster.managementsystem.support.ApiResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiProvider.User.ROOTPATH)
public class AuthController extends AbstractController{

    private final AuthDomain authDomain;

    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = UserDto.class)))
    })
    @PostMapping(ApiProvider.User.USER_REGISTRATION)
    public ResponseEntity<ApiResponseDto<UserDto>> registerUser(@RequestBody UserParam param) throws Exception {
        log.debug("Request to Create an User... {}", param);
        // Return result and message.
        return generateResponse(authDomain.register(param), HttpStatus.CREATED, "User created Successful!");
    }

    @Operation(summary = "Create User")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Success", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Data not found", content = @Content(schema = @Schema(implementation = JwtResponse.class)))
    })
    @PostMapping(ApiProvider.User.USER_LOGIN)
    public ResponseEntity<ApiResponseDto<JwtResponse>> login(@RequestBody LoginParam param) throws Exception {
        log.debug("Request to Create an User... {}", param);
        // Return result and message.
        return generateResponse(authDomain.login(param), HttpStatus.CREATED, "Login Successful!");
    }
}