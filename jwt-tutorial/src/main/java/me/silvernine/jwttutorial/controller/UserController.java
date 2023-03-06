package me.silvernine.jwttutorial.controller;

import lombok.RequiredArgsConstructor;
import me.silvernine.jwttutorial.dto.UserDto;
import me.silvernine.jwttutorial.entity.User;
import me.silvernine.jwttutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    public static final String HAS_ANY_ROLE_USER_ADMIN = "hasAnyRole('USER', 'ADMIN')";
    public static final String HAS_ANY_ROLE_ADMIN = "hasAnyRole('ADMIN')";
    private final UserService userService;

    @PostMapping("/test-redirect")
    public void testRedirect(HttpServletResponse response) throws IOException{
        response.sendRedirect("/api/user");
    }


    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@Validated @RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.signup(userDto));
    }

    @GetMapping("/user")
    @PreAuthorize(HAS_ANY_ROLE_USER_ADMIN)
    public ResponseEntity<UserDto> getMyUserInfo(HttpServletRequest request){
        return ResponseEntity.ok(userService.getMyUserWithAuthorities());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize(HAS_ANY_ROLE_ADMIN)
    public ResponseEntity<UserDto> getUserInfo(@PathVariable String username){
        return ResponseEntity.ok(userService.getUserWithAuthorities(username));
    }


}
