package com.devkhoa.statuscollector.statusretriever.config;

import com.devkhoa.statuscollector.configdata.InMemoryUserConfigData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final InMemoryUserConfigData inMemoryUserConfigData;

    public SecurityConfig(InMemoryUserConfigData inMemoryUserConfigData) {
        this.inMemoryUserConfigData = inMemoryUserConfigData;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .antMatchers("/**").hasRole("USER")
                        .anyRequest().authenticated()
                )
                .httpBasic().and()
                .csrf().disable();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        manager.createUser(User.builder()
                .username(inMemoryUserConfigData.getUsername())
                .password(passwordEncoder().encode(inMemoryUserConfigData.getPassword()))
                .roles(inMemoryUserConfigData.getRoles().toArray(new String[0]))
                .build());
        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
