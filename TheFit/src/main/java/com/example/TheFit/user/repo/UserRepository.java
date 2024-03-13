package com.example.TheFit.user.repo;

import com.example.TheFit.user.dto.UserIdPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserIdPassword,Long> {
    Optional<UserIdPassword> findByEmail(String email);
}
