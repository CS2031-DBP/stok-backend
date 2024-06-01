package com.example.stokapp.user.domain;

import com.example.stokapp.employee.infrastructure.EmployeeRepository;
import com.example.stokapp.owner.infrastructure.OwnerRepository;
import com.example.stokapp.user.infrastructure.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository<User> userRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private OwnerRepository ownerRepository;

    public User findByEmail(String username, String role) {
        User user;
        if (role.equals("ROLE_OWNER"))
            user = ownerRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        else
            user = employeeRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User nottt found"));

        return user;
    }

    @Bean(name = "UserDetailsService")
    public UserDetailsService userDetailsService() {
        return username -> {
            User user = userRepository
                    .findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            return (UserDetails) user;
        };
    }
}
