package com.sergio.spring.rest.usuariosvehiculos.app.auth.services;

import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class JpaUserDetailService implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Traemos el usernam "email"
        Optional<com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User> o = userRepository.getUserByEmail(username);

        if (!o.isPresent()) {
            throw new UsernameNotFoundException(String.format("Username %s no existe en el sistema", username));
        }
        com.sergio.spring.rest.usuariosvehiculos.app.models.entities.User user = o.orElseThrow();

        //Obtener roles desde la base de datos
        List<GrantedAuthority> authorities = user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());

        return new User(user.getEmail(), user.getPassword(), true, true, true, true, authorities);
    }
}
