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
    public UUID createUser(ServerUser user) {
        ServerUser savedUser = userRepository.save(user);
        return savedUser.getUuid();
    }

    @Override
    public Optional<ServerUser> getUser(UUID uuid) {
        return userRepository.findById(uuid);
    }

    @Override
    public Optional<ServerUser> getUser(String email) {
        return userRepository.findByEmail(email);
    }
}
