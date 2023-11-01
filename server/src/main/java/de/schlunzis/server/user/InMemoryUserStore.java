package de.schlunzis.server.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class InMemoryUserStore implements IUserStore {

    private final Map<UUID, ServerUser> userMap = new HashMap<>();
    private final PasswordEncoder passwordEncoder;

    @Override
    public UUID createUser(ServerUser user) {
        if (userMap.values().stream().anyMatch(u -> u.getEmail().equals(user.getEmail()))) {
            throw new IllegalArgumentException("User with email " + user.getEmail() + " already exists");
        }

        UUID uuid;
        do {
            uuid = UUID.randomUUID();
        } while (userMap.containsKey(uuid));
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userMap.put(uuid, user);
        return uuid;
    }

    @Override
    public Optional<ServerUser> getUser(UUID uuid) {
        return Optional.ofNullable(userMap.get(uuid));
    }

    @Override
    public Optional<ServerUser> getUser(String email) {
        return userMap.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }

}
