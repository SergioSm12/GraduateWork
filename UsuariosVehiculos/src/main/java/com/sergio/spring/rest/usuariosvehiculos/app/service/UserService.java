package com.sergio.spring.rest.usuariosvehiculos.app.service;

import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.UserDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.entity.users.VehicleTypeDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.dto.mapper.DtoMapperVehicleDto;
import com.sergio.spring.rest.usuariosvehiculos.app.models.entities.*;
import com.sergio.spring.rest.usuariosvehiculos.app.models.interfaces.IUser;
import com.sergio.spring.rest.usuariosvehiculos.app.models.request.UserRequest;
import com.sergio.spring.rest.usuariosvehiculos.app.repositorys.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements IUserService {
    @Autowired
    private IUserRepository userRepository;
    @Autowired
    private IReceiptRepository receiptRepository;
    @Autowired
    private IRoleRepository roleRepository;

    //vehicle
    @Autowired
    private IVehicleRepository vehicleRepository;

    //Rate
    @Autowired
    private IRateRepository rateRepository;

    @Autowired
    private IVehicleTypeRepository vehicleTypeRepository;

    //Encriptar
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = (List<User>) userRepository.findAll();
        return users
                .stream()
                .map(u -> DtoMapperUser.builder()
                        .setUser(u)
                        .build()).collect(Collectors.toList());
    }



    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        Page<User> usersPage = userRepository.findAll(pageable);
        return usersPage.map(u -> DtoMapperUser.builder().setUser(u).build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> findById(Long id) {
        return userRepository.findById(id).map(u ->
                DtoMapperUser
                        .builder()
                        .setUser(u)
                        .build());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByIdUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public UserDto save(User user) {
        String passwordBCrypt = passwordEncoder.encode(user.getPassword());
        user.setPassword(passwordBCrypt);
        user.setRoles(getRoles(user));
        String emailLower = user.getEmail().toLowerCase();
        user.setEmail(emailLower);
        //DESCRIPCION return: trae la instancia asigna el user y construye
        return DtoMapperUser.builder().setUser(userRepository.save(user)).build();
    }

    @Override
    @Transactional
    public Optional<UserDto> update(UserRequest user, Long id) {
        Optional<User> o = userRepository.findById(id);
        User userOptional = null;

        if (o.isPresent()) {
            User userDb = o.orElseThrow();
            userDb.setRoles(getRoles(user));
            userDb.setName(user.getName());
            userDb.setLastName(user.getLastName());
            userDb.setEmail(user.getEmail());
            userDb.setPhoneNumber(user.getPhoneNumber());
            userOptional = userRepository.save(userDb);
        }
        return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    }

    @Override
    @Transactional
    public void remove(Long id) {
        userRepository.deleteById(id);
    }

    //Vehicle
    //Listar vehiculos por usuario
    @Override
    @Transactional(readOnly = true)
    public List<VehicleDto> findVehiclesByUserId(Long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        List<Vehicle> vehicles = userOptional.get().getVehicles();
        return vehicles.stream().map(v -> DtoMapperVehicleDto.builder().setVehicle(v).build()).collect(Collectors.toList());
    }

    //listar un vehiculo por id
    @Override
    @Transactional(readOnly = true)
    public Optional<Vehicle> findVehicleByIdAndUserId(Long vehicleId, Long userId) {
        return vehicleRepository.findByIdAndUserId(vehicleId, userId);
    }

    //Guardar vehiculos por usuario
    @Override
    @Transactional
    public VehicleDto saveVehicle(Vehicle vehicle) {
        String plateUpper = vehicle.getPlate().toUpperCase();
        vehicle.setPlate(plateUpper);

        //optener user y asociarlo
        UserDto userDto = DtoMapperUser.builder().setUser(vehicle.getUser()).build();
        Optional<User> userOptional = userRepository.findById(userDto.getId());

        userOptional.ifPresent(vehicle::setUser);

        return DtoMapperVehicleDto.builder().setVehicle(vehicleRepository.save(vehicle)).build();
    }

    //update vehicles por usuario
    @Override
    @Transactional
    public Optional<VehicleDto> updateVehicle(Long userId, Long vehicleId, Vehicle vehicle) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            return Optional.empty();
        }
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            return Optional.empty();
        }

        User user = userOptional.get();
        Vehicle vehicleaUpdate = vehicleOptional.get();
        if (!vehicleaUpdate.getUser().equals(user)) {
            return Optional.empty();
        }

        //Actualizar placa
        vehicleaUpdate.setPlate(vehicle.getPlate());
        //Actualizar type
        if (vehicle.getVehicleType() != null) {
            VehicleType vehicleType = vehicle.getVehicleType();
            vehicleaUpdate.setVehicleType(vehicleType);
        }

        Vehicle updateVehicle = vehicleRepository.save(vehicleaUpdate);

        return Optional.of(DtoMapperVehicleDto.builder().setVehicle(updateVehicle).build());
    }

    //Delete vehicle id
    @Override
    @Transactional
    public void removeVehicleByUser(Long vehicleId) {
        vehicleRepository.deleteByVehicleId(vehicleId);
    }

    //Traer vehicle
    @Override
    @Transactional(readOnly = true)
    public List<VehicleType> findAllVehicleType() {
        return (List<VehicleType>) vehicleTypeRepository.findAll();
    }

    //Create

    //Roles
    private List<Role> getRoles(IUser user) {
        Optional<Role> ou = roleRepository.findByName("ROLE_USER");
        List<Role> roles = new ArrayList<>();

        if (ou.isPresent()) {
            roles.add(ou.orElseThrow());
        }

        //Añadir admin
        if (user.isAdmin()) {
            Optional<Role> oa = roleRepository.findByName("ROLE_ADMIN");
            if (oa.isPresent()) {
                roles.add(oa.orElseThrow());
            }
        }
        //Añadir guard
        if (user.isGuard()) {
            Optional<Role> og = roleRepository.findByName("ROLE_GUARD");
            if (og.isPresent()) {
                roles.add(og.orElseThrow());
            }
        }

        return roles;
    }
}
