package com.duofan.starter.controller.v1.api;

import com.duofan.starter.controller.v1.command.PasswordFormCommand;
import com.duofan.starter.controller.v1.command.ProfileFormCommand;
import com.duofan.starter.controller.v1.request.UserSignupRequest;
import com.duofan.starter.dto.model.common.UserDto;
import com.duofan.starter.dto.response.Response;
import com.duofan.starter.service.UserService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;


@RestController
@RequestMapping("/api/v1/user")
@Api(value = "application")
public class UserController {
    @Autowired
    private UserService userService;

    @ApiOperation("Login")
    @PostMapping("/login")
    public void fakeLogin(@RequestBody @Valid LoginRequest loginRequest) {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @ApiOperation("Logout")
    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public void fakeLogout() {
        throw new IllegalStateException("This method shouldn't be called. It's implemented by Spring Security filters.");
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class LoginRequest {
        @NotNull(message = "{constraints.NotEmpty.message}")
        private String username;
        @NotNull(message = "{constraints.NotEmpty.message}")
        private String password;
    }


    /**
     * Handles the incoming POST API "/v1/user/signup"
     *
     * @param userSignupRequest
     * @return
     */
    @PostMapping("/signup")
    public Response signup(@RequestBody @Validated UserSignupRequest userSignupRequest) {
        return Response.ok().setData(registerUser(userSignupRequest, false));
    }

    /**
     * Register a new user in the database
     *
     * @param userSignupRequest
     * @return
     */
    private UserDto registerUser(UserSignupRequest userSignupRequest, boolean isAdmin) {
        UserDto userDto = new UserDto()
                .setUsername(userSignupRequest.getUsername())
                .setPassword(userSignupRequest.getPassword())
                .setAdmin(isAdmin);
        return userService.signup(userDto);
    }

    @GetMapping(value = "/profile")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    @PreAuthorize("isAuthenticated()")
    public Response getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Response.ok().setData(userService.findUserByUsername(auth.getName()));
    }


    @PostMapping(value = "/profile")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response updateProfile(@RequestBody @Valid ProfileFormCommand profileFormCommand) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByUsername(auth.getName());
        userDto
                .setMobileNumber(profileFormCommand.getMobileNumber());
        return Response.ok().setData(userService.updateProfile(userDto));
    }

    @PostMapping(value = "/password")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response changePassword(@Valid PasswordFormCommand passwordFormCommand) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDto userDto = userService.findUserByUsername(auth.getName());
        userService.changePassword(userDto, passwordFormCommand.getPassword());
        SecurityContextHolder.getContext().setAuthentication(null);
        return Response.ok();
    }
}
