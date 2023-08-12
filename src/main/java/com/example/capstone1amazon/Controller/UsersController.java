package com.example.capstone1amazon.Controller;

import com.example.capstone1amazon.ApiResponse.ApiErrorResponse;
import com.example.capstone1amazon.ApiResponse.ApiResponseWithData;
import com.example.capstone1amazon.DTO.UpdateUserDTO;
import com.example.capstone1amazon.Model.User;
import com.example.capstone1amazon.Service.ErrorsService;
import com.example.capstone1amazon.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UsersController {

    private final UserService userService;
    private final ErrorsService errorsService;

    // GET /api/v1/users/get?role=admin
    // ---- OR ----
    // GET /api/v1/users/get?role=customer
    // ---- OR ----
    // GET /api/v1/users/get
    @GetMapping("/get")
    public ResponseEntity<Collection<User>> getAllUsers(@RequestParam(value = "role", required = false) String role) {
        return ResponseEntity.ok(userService.getAllUsers(role));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody @Valid User user, Errors errors) {
        if(userService.containsId(user.getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "the id must be unique.", "id", "unique")));
        }

        if(userService.containsEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "used email.", "email", "duplicate_value")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsService.bulkAdd(errors).get());
        }

        userService.createUser(user);

        return ResponseEntity.status(HttpStatus.CREATED).body((new ApiResponseWithData<User>("the user have beenc created.", user)));
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody @Valid UpdateUserDTO updateUserDTO, Errors errors) {
        if(!userService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
        }

        if(userService.containsEmail(updateUserDTO.getEmail(), id)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("user", "used email.", "email", "duplicate_value")));
        }

        if(errors.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorsService.bulkAdd(errors).get());
        }

        User user = userService.updateUser(id, updateUserDTO);

        return ResponseEntity.ok((new ApiResponseWithData<User>("the user have been updated.", user)));
    }


    @DeleteMapping("/{id}/delete")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        if(!userService.containsId(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
        }

        User user = userService.deleteUser(id);

        return ResponseEntity.ok((new ApiResponseWithData<User>("the user have been deleted.", user)));
    }

    @GetMapping("/get/{field}/{value}")
    public ResponseEntity<?> getUserByField(@PathVariable String field, @PathVariable String value) {
        if(field.equalsIgnoreCase("email")) {
            if(!userService.containsEmail(value)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "email", "not_found")));
            }

            return ResponseEntity.ok(userService.getUserByEmail(value));
        }

        if(field.equalsIgnoreCase("id")) {
            try {
                if(!userService.containsId(Integer.parseInt(value))) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body((new ApiErrorResponse("user", "user not found.", "id", "not_found")));
                }

                return ResponseEntity.ok(userService.getUserById(Integer.parseInt(value)));
            } catch (NumberFormatException e1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("request", "invalid id format, it must be number.", "id", "invalid_format")));
            }
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body((new ApiErrorResponse("request", "invalid field, it can only be 'id' or 'email'.", field, "invalid_field")));
    }

}
