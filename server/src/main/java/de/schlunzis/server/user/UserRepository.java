package de.schlunzis.server.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<DBUser, UUID> {

    Optional<DBUser> findByEmail(String email);

}
