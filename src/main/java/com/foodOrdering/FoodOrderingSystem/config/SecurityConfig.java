package com.foodOrdering.FoodOrderingSystem.config;

import com.foodOrdering.FoodOrderingSystem.security.JwtFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtFilter jwtFilter;
    private final SecurityExceptionHandler securityExceptionHandler;
    public SecurityConfig(JwtFilter jwtFilter, SecurityExceptionHandler securityExceptionHandler){
        this.jwtFilter=jwtFilter;
        this.securityExceptionHandler=securityExceptionHandler;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> {})  // localhost change krne ke liye
                .logout(logout -> logout.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/register").permitAll()
                        .requestMatchers("/login").permitAll()
                        .requestMatchers("/refresh").permitAll()
                        .anyRequest().authenticated()
                )
                // exception handle krne ke liye
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(securityExceptionHandler)
                        .accessDeniedHandler(securityExceptionHandler)
                )
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);  //JwtFilter ko UsernamePasswordAuthenticationFilter se pehle chalaya ja raha hai.

        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}