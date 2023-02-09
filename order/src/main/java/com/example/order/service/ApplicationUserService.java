package com.example.order.service;

import com.example.order.exception.UserAlreadyExistException;
import com.example.order.model.ApplicationUser;
import com.example.order.dto.UserDto;
import com.example.order.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ApplicationUserService implements UserDetailsService {
    private final UserRepository repository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public ApplicationUser loadUserByUsername(String username) {
        var user = repository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return user;
    }

    public Long getCurrentUserId() {
        var principal = (ApplicationUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return principal.getId();
    }

    @Transactional
    public ApplicationUser registerNewUserAccount(UserDto userDto) {
        if (usernameExists(userDto.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that username: "
                    + userDto.getUsername());
        }
        var user = modelMapper.map(userDto, ApplicationUser.class);
        user.setRole("USER");
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return repository.save(user);
    }

    private boolean usernameExists(String username) {
        return repository.findByUsername(username) != null;
    }


}