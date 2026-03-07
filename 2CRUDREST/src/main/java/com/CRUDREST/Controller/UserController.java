package com.CRUDREST.Controller;

import com.CRUDREST.Model.User;
import com.CRUDREST.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;
    UserController(UserService userService) {
    this.userService = userService;
    }


    @PostMapping("/users")
    public ResponseEntity<User>createUser( @RequestBody User user)
    {
        return userService.createUser(user);
    }


    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers()
    {
        return userService.getAllUsers();
    }

    @PutMapping("/users/{id}")
    public Optional<ResponseEntity<User>> updateUser(@PathVariable int id, @RequestBody User user)
    {
        return userService.updateUser(id, user);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id)
    {
        return userService.deleteUser(id);
    }
}
