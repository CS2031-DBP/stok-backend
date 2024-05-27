package com.example.stokapp.auth;
import com.example.stokapp.auth.dto.AuthJwtResponse;
import com.example.stokapp.auth.dto.AuthLoginRequest;
import com.example.stokapp.auth.dto.AuthRegisterRequest;
import com.example.stokapp.configuration.JwtService;
import com.example.stokapp.employee.domain.Employee;
import com.example.stokapp.event.WelcomeEmailEvent;
import com.example.stokapp.owner.domain.Owner;
import com.example.stokapp.user.domain.Role;
import com.example.stokapp.user.domain.User;
import com.example.stokapp.user.infrastructure.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
public class AuthService {

    final UserRepository<User> baseUserRepository;
    final JwtService jwtService;
    final PasswordEncoder passwordEncoder;
    final ModelMapper modelMapper;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.baseUserRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    public AuthJwtResponse login(AuthLoginRequest req) {
        Optional<User> user = baseUserRepository.findByEmail(req.getUsername());

        if (user.isEmpty()) throw new UsernameNotFoundException("Email is not registered");

        if (!passwordEncoder.matches(req.getPassword(), user.get().getPassword()))
            throw new IllegalArgumentException("Password is incorrect");

        AuthJwtResponse response = new AuthJwtResponse();

        response.setToken(jwtService.generateToken(user.get()));
        response.setId(user.get().getId());
        response.setRole(user.get().getRole());
        return response;
    }

    public AuthJwtResponse register(AuthRegisterRequest req){
        Optional<User> user = baseUserRepository.findByEmail(req.getEmail());
        if (user.isPresent()) throw new IllegalArgumentException("Email is already registered");

        if (req.getIsOwner()) {
            Owner owner = new Owner();
            owner.setCreatedAt(ZonedDateTime.now());
            owner.setRole(Role.OWNER);
            owner.setFirstName(req.getFirstName());
            owner.setLastName(req.getLastName());
            owner.setEmail(req.getEmail());
            owner.setPassword(passwordEncoder.encode(req.getPassword()));
            owner.setPhoneNumber(req.getPhoneNumber());

            baseUserRepository.save(owner);

            AuthJwtResponse response = new AuthJwtResponse();
            response.setToken(jwtService.generateToken(owner));
            response.setId(owner.getId());
            response.setRole(owner.getRole());
            applicationEventPublisher.publishEvent(new WelcomeEmailEvent(this, owner.getEmail(), owner.getFirstName()));
            return response;
        }
        else {
            Employee employee = new Employee();
            employee.setCreatedAt(ZonedDateTime.now());
            employee.setRole(Role.EMPLOYEE);
            employee.setFirstName(req.getFirstName());
            employee.setLastName(req.getLastName());
            employee.setEmail(req.getEmail());
            employee.setPassword(passwordEncoder.encode(req.getPassword()));
            employee.setPhoneNumber(req.getPhoneNumber());

            baseUserRepository.save(employee);

            AuthJwtResponse response = new AuthJwtResponse();
            response.setToken(jwtService.generateToken(employee));
            response.setId(employee.getId());
            response.setRole(employee.getRole());
            applicationEventPublisher.publishEvent(new WelcomeEmailEvent(this, employee.getEmail(), employee.getFirstName()));
            return response;
        }
    }
}