package org.schlunzis.kurtama.server.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Profile("!production")
@RequiredArgsConstructor
public class H2UserStore implements IUserStore {

    private final UserRepository userRepository;

    @Override
    public UUID createUser(DBUser user) {
        DBUser dbUser = userRepository.save(user);
        return dbUser.getId();
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
