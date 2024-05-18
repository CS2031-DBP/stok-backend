package com.example.stokapp.user.infrastructure;

import com.example.stokapp.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository <T extends User> extends JpaRepository<T, Long> {
}
