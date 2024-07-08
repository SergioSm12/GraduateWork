package com.sergio.spring.rest.usuariosvehiculos.app.config;

import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.Role;
import com.sergio.spring.rest.usuariosvehiculos.app.repositories.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        List<String> roles = Arrays.asList("ROLE_ADMIN", "ROLE_GUARD", "ROLE_USER");

        for (String roleName : roles){
            Optional<Role> roleOptional = roleRepository.findByName(roleName);
            if(!roleOptional.isPresent()){
                Role role = new Role();
                role.setName(roleName);
                roleRepository.save(role);
            }

        }
    }
}
