package com.CRUDREST.Controller;

import com.CRUDREST.Model.User;
import com.CRUDREST.Service.ElevenLabsService;
import com.CRUDREST.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
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



    @Autowired
    private ElevenLabsService elevenLabsService;

    @GetMapping("/models")
    public ResponseEntity<?> getModels() {
        String response = elevenLabsService.getModels();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/speak")
    public ResponseEntity<byte[]> speak(@RequestParam String text) {

        byte[] audio = elevenLabsService.generateSpeech(text);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, "audio/mpeg")
                .body(audio);
}

}

