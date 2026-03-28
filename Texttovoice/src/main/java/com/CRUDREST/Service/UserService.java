package com.CRUDREST.Service;

import com.CRUDREST.Model.User;
import com.CRUDREST.Repository.UserRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepo userRepo;
    UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public ResponseEntity<User> createUser(User user) {
        User savedUser = userRepo.save(user);
        return ResponseEntity.ok(savedUser);
    }

    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(userRepo.findAll());
    }

    public Optional<ResponseEntity<User>> updateUser(int id, User user) {
        return userRepo.findById(id).map(existingUser ->{
            existingUser.setFirstName(user.getFirstName());
            existingUser.setLastName(user.getLastName());
            existingUser.setEmailId(user.getEmailId());
            existingUser.setUpdatedAt(user.getUpdatedAt());
            existingUser.setUpdatedby(user.getUpdatedby());
            User updatedUser = userRepo.save(existingUser);
            return ResponseEntity.ok(updatedUser);
        });
    }

    public ResponseEntity<?> deleteUser(int id) {
        return userRepo.findById(id).map(user -> {
            userRepo.delete(user);
            return ResponseEntity.ok().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
