package com.clover.plogger;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
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
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) ->
                        csrf.disable()
                )
                .headers((header) ->
                                header.frameOptions(frameOptions ->
                                frameOptions.disable())
                )
                .authorizeHttpRequests((authorize) ->
                authorize
                        .requestMatchers(PathRequest.toH2Console()).permitAll()
                        .requestMatchers("/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((formLogin) -> formLogin.loginPage("/login")
                        .defaultSuccessUrl("/")
                        .failureUrl("/fail")
                )
                .logout( logout -> logout.logoutUrl("/logout")
        );

        return http.build();
    }

}