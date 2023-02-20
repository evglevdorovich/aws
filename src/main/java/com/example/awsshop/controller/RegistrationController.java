package com.example.awsshop.controller;

import com.example.awsshop.exception.UserAlreadyExistException;
import com.example.awsshop.model.ApplicationUser;
import com.example.awsshop.model.UserDto;
import com.example.awsshop.model.UserVirtualPaymentAccount;
import com.example.awsshop.service.ApplicationUserService;
import com.example.awsshop.service.UserVirtualPaymentAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Controller
public class RegistrationController {
    private final ApplicationUserService userService;
    private final UserVirtualPaymentAccountService userVirtualPaymentAccountService;

    @GetMapping("/")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new UserDto());
        return "registration";
    }

    @PostMapping("/users/registration")
    @ResponseBody
    public ResponseEntity<UserDto> registerUserAccount(@RequestBody UserDto userDto) {
        ResponseEntity<UserDto> userDtoResponseEntity;
        ApplicationUser applicationUser = null;
        try {
            applicationUser = userService.registerNewUserAccount(userDto);
            userDtoResponseEntity = ResponseEntity.ok(userDto);
        } catch (UserAlreadyExistException e) {
            userDto.setErrorMessage(e.getMessage());
            userDtoResponseEntity = ResponseEntity.status(400).body(userDto);
        }
        userDto.setPassword(null);
        assert applicationUser != null;
        simulateCreationOfVirtualPaymentAccount(applicationUser);

        return userDtoResponseEntity;
    }

    private void simulateCreationOfVirtualPaymentAccount(ApplicationUser applicationUser) {
        userVirtualPaymentAccountService.save(new UserVirtualPaymentAccount(applicationUser.getId()
                , BigDecimal.valueOf(1000_000_000)));
    }
}
