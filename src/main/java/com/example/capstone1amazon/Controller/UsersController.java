package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.Model.User;
import com.example.capstone1amazon.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;

    @GetMapping("/get")
    public ResponseEntity<Collection<User>> getAllUsers(@RequestParam(value = "role", required = false) String role) {
        return ResponseEntity.ok(userService.getAllUsers(role));
    }
}
