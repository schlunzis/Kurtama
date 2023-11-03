package org.schlunzis.kurtama.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<ServerUser, UUID> {

    Optional<ServerUser> findByEmail(String email);

}