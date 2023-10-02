package com.example.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .headers().frameOptions().disable()
                .and()
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/warehouse**")
                        .hasRole("WAREHOUSE_MANAGER"))
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/products**","/checkout**","/shoppingCarts**")
                        .authenticated())
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/**")
                        .permitAll())
                .formLogin()
                .defaultSuccessUrl("/products")
                .and()
                .logout()
                .logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .invalidateHttpSession(true)
                .logoutSuccessUrl("/login");

        return http.build();
    }

}
