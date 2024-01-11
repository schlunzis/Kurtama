package org.schlunzis.kurtama.server.auth;

import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
class UserSessionMap {

    private final ConcurrentHashMap<ServerUser, ISession> map = new ConcurrentHashMap<>();

    public void put(ServerUser user, ISession session) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(session);
        map.put(user, session);
    }

    public Optional<ISession> get(ServerUser user) {
        Objects.requireNonNull(user);
        return Optional.ofNullable(map.get(user));
    }

    public Optional<ServerUser> get(ISession session) {
        Objects.requireNonNull(session);

        Optional<Map.Entry<ServerUser, ISession>> optionalEntry = map.entrySet().stream().filter(e -> e.getValue().equals(session)).findFirst();
        return optionalEntry.map(Map.Entry::getKey);
    }

    public Collection<ISession> getFor(Collection<ServerUser> users) {
        Objects.requireNonNull(users);

        return users.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public Collection<ISession> getAllSessions() {
        return map.values();
    }

    public boolean contains(ISession session) {
        Objects.requireNonNull(session);
        return map.containsValue(session);
    }

    public void remove(ServerUser user) {
        Objects.requireNonNull(user);
        map.remove(user);
    }

    public void remove(ISession session) {
        Objects.requireNonNull(session);
        map.entrySet().removeIf(e -> e.getValue().equals(session));
    }

}
