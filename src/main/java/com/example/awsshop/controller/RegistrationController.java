package com.example.awsshop.controller;

import com.example.awsshop.exception.UserAlreadyExistException;
import com.example.awsshop.model.UserDto;
import com.example.awsshop.service.ApplicationUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class RegistrationController {
    private final ApplicationUserService userService;

    @GetMapping("/")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration";
    }

    @PostMapping("/users/registration")
    @ResponseBody
    public ResponseEntity<UserDto> registerUserAccount(@RequestBody UserDto userDto) {
        ResponseEntity<UserDto> userDtoResponseEntity;
        try {
            userService.registerNewUserAccount(userDto);
            userDtoResponseEntity = ResponseEntity.ok(userDto);
        } catch (UserAlreadyExistException e) {
            userDto.setErrorMessage(e.getMessage());
            userDtoResponseEntity = ResponseEntity.status(400).body(userDto);
        }
        userDto.setPassword(null);
        return userDtoResponseEntity;
    }
}
