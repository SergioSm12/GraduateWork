package com.sergio.spring.rest.usuariosvehiculos.app.auth;

import java.util.Arrays;

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

import com.sergio.spring.rest.usuariosvehiculos.app.auth.filters.JwtAuthenticationFilter;
import com.sergio.spring.rest.usuariosvehiculos.app.auth.filters.JwtValidationFilter;

@Configuration
public class SpringSecurityConfig {
    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    // Codificar el password
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
                        // usuario
                        .requestMatchers(HttpMethod.GET, "/users").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.GET, "/users/count-total").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/active-users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/inactive-users").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/page/{page}").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.GET, "/users/{id}").hasAnyRole("ADMIN","GUARD","USER")
                        .requestMatchers(HttpMethod.POST, "/users").permitAll()
                        .requestMatchers(HttpMethod.PUT, "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/users/changePassword/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/activate/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/users/deactivate/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/users/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/users/email").hasAnyRole("ADMIN","USER","GUARD")

                        // usuario vehicle
                        .requestMatchers(HttpMethod.POST, "/vehicle/{userId}/create").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/vehicle/{userId}/list").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/vehicle/{userId}/active-vehicles").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/vehicle/{userId}/inactive-vehicles").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.PUT, "/vehicle/{userId}/update/{vehicleId}").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.DELETE, "/vehicle/{userId}/delete/{vehicleId}").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.GET, "/vehicle/{userId}/activate-vehicle/{vehicleId}").hasAnyRole("ADMIN","USER")

                        //vehicle
                        .requestMatchers(HttpMethod.GET, "/vehicle/list").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/vehicle/count-total").hasAnyRole("ADMIN","GUARD")

                        // vehicle type
                        .requestMatchers(HttpMethod.GET, "/vehicleType").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.POST, "/vehicleType").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/vehicleType/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/vehicleType/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/rate").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/rate/{rateId}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.POST, "/rate").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/rate/{rateId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/rate/{rateId}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/receipt").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/receipt/unpaid").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/receipt/paid").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/receipt/count-unpaid").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/receipt/count-paid").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/receipt/count-total").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.PUT, "/receipt/{receiptId}/update").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.PUT, "/receipt/change-payment/{receiptId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/receipt/{receiptId}").hasRole("ADMIN")

                        // usuario
                        .requestMatchers(HttpMethod.GET, "/receipt/user/{userId}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/receipt/user/{userId}/unpaid").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.POST, "/receipt/{userId}/create").hasAnyRole("ADMIN","USER","GUARD")

                        //QRCode
                        .requestMatchers(HttpMethod.GET, "/qrcode/{id}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/qrcode/visitor/{id}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/qrcode/nightly-receipt/{id}").hasAnyRole("ADMIN","USER","GUARD")

                        // visitor
                        .requestMatchers(HttpMethod.GET, "visitor-receipt").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.GET, "visitor-receipt/count-total").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "visitor-receipt/count-unpaid").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "visitor-receipt/count-paid").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "visitor-receipt").permitAll()
                        .requestMatchers(HttpMethod.PUT, "visitor-receipt/{id}").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.PUT, "visitor-receipt/change-payment/{visitorReceiptId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "visitor-receipt/{visitorReceiptId}").hasRole("ADMIN")

                        //Nightly Receipt
                        .requestMatchers(HttpMethod.GET, "nightly-receipt").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "nightly-receipt/{userId}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.POST, "nightly-receipt/{userId}/create").hasAnyRole("ADMIN","USER")
                        .requestMatchers(HttpMethod.PUT, "/nightly-receipt/{nightlyReceiptId}/update").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.PATCH, "nightly-receipt/change-payment/{receiptId}").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.GET, "nightly-receipt/count-total").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "nightly-receipt/count-unpaid").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "nightly-receipt/count-paid").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/nightly-receipt/{nightlyReceiptId}").hasRole("ADMIN")

                        //reports
                        .requestMatchers(HttpMethod.GET, "reports/income/monthly").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "reports/income/monthly").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/weekly").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/biweekly").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "reports/income/monthly/visitor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "reports/income/monthly/visitor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/weekly/visitor").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/biweekly/visitor").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "reports/income/monthly/nightly").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "reports/income/monthly/nightly").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/weekly/nightly").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/biweekly/nightly").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "reports/income/monthly/unified").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "reports/income/monthly/unified").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/biweekly/unified").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "reports/income/weekly/unified").hasRole("ADMIN")

                //EDIFICIOS
                        .requestMatchers(HttpMethod.GET, "/building").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/building/{buildingId}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.POST, "/building").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/building/{buildingId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/building/{buildingId}").hasRole("ADMIN")

                //AFORO
                        .requestMatchers(HttpMethod.GET, "/capacity").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.GET, "/capacity/{capacityId}").hasAnyRole("ADMIN","USER","GUARD")
                        .requestMatchers(HttpMethod.POST, "/capacity").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.PUT, "/capacity/{capacityId}").hasAnyRole("ADMIN","GUARD")
                        .requestMatchers(HttpMethod.DELETE, "/capacity/{capacityId}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/capacity/vehicle-entry/{capacityId}").hasRole("GUARD")
                        .requestMatchers(HttpMethod.PATCH, "/capacity/vehicle-exit/{capacityId}").hasRole("GUARD")


                        .anyRequest().authenticated())
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .build();
    }

    // Configurar CORS
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    // Filtro de prioridad
    @Bean
    FilterRegistrationBean<CorsFilter> corsFilter() {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
                new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
