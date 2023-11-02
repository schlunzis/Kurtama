package de.schlunzis.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class H2UserStore implements IUserStore {

    private final UserRepository userRepository;

    @Override
    public UUID createUser(DBUser user) {
        DBUser dbUser = userRepository.save(user);
        return dbUser.getUuid();
    }

    @Override
    public Optional<DBUser> getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public Optional<DBUser> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
