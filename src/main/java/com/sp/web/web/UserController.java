package com.sp.web.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sp.web.domain.User;
import com.sp.web.domain.UserService;

@RestController
@RequestMapping("api/users")
@CrossOrigin("http://localhost:5173")
public class UserController {
    private final UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    

    @GetMapping("/showUsers")
    public List<User> showUsers() {
        return userService.getUsers();
    }

    @PostMapping
    public ResponseEntity<?> registerUser(@RequestBody UserCMD userCMD){
        
        if (userService.existsByUsername(userCMD.getUsername())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exist!!!");
        }
        if (userService.existsByEmail(userCMD.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exist!!!");
        }

        User newUser = new User(userCMD.getUsername(), userCMD.getPassword(), userCMD.getEmail(), userCMD.getName(), userCMD.getAge());

        User registeredUser = userService.registerUser(newUser);

        return ResponseEntity.ok(registeredUser);
    }
}
