package com.sergio.spring.rest.usuariosvehiculos.app.auth;

import com.sergio.spring.rest.usuariosvehiculos.app.auth.filters.JwtAuthenticationFilter;
import com.sergio.spring.rest.usuariosvehiculos.app.auth.filters.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    //Codificar el password
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(authRules -> authRules
                        .requestMatchers(HttpMethod.GET, "/faculty").permitAll()
                        .requestMatchers(HttpMethod.GET, "/faculty/{facultyId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/faculty").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/faculty/{facultyId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/faculty/{facultyId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/page/{page}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN", "GUARD")
                        .requestMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/vehicle/{userId}/create").permitAll()
                        .requestMatchers(HttpMethod.GET, "/vehicle/{userId}/list").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/vehicle/{userId}/update/{vehicleId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/vehicle/{userId}/delete/{vehicleId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rate").permitAll()
                        .requestMatchers(HttpMethod.GET, "/rate/{rateId}").permitAll()
                        .requestMatchers(HttpMethod.POST, "/rate").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/rate/{rateId}").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/rate/{rateId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/receipt").permitAll()
                        .requestMatchers(HttpMethod.GET, "/receipt/unpaid").permitAll()
                        .requestMatchers(HttpMethod.GET, "/receipt/paid").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/receipt/{receiptId}/update").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/receipt/{receiptId}").permitAll()

                        .requestMatchers(HttpMethod.GET, "/receipt/user/{userId}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/receipt/user/{userId}/unpaid").permitAll()
                        .requestMatchers(HttpMethod.POST, "/receipt/{userId}/create").permitAll()

                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    //Configurar CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    //Filtro de prioridad
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
