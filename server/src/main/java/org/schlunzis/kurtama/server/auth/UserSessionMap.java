package org.schlunzis.kurtama.server.auth;

import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.schlunzis.kurtama.server.util.BiMap;
import org.schlunzis.kurtama.server.util.ConcurrentBiHashMap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Component
class UserSessionMap {

    private final BiMap<ServerUser, ISession> map = new ConcurrentBiHashMap<>();

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
        return Optional.ofNullable(map.getByValue(session));
    }

    public Collection<ISession> getFor(Collection<ServerUser> users) {
        Objects.requireNonNull(users);
        return map.getForKeys(users);
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
        map.removeByValue(session);
    }

}
