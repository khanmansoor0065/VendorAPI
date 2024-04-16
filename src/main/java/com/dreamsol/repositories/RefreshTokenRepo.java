package com.dreamsol.repositories;

import com.dreamsol.entities.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken,String> {

    Optional<RefreshToken> findByRefreshToken(String token);
}
