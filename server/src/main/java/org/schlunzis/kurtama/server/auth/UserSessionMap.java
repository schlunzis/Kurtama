package org.schlunzis.kurtama.server.auth;

import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class UserSessionMap {

    private final Map<ServerUser, ISession> map = new HashMap<>();

    public void put(ServerUser user, ISession session) {
        map.put(user, session);
    }

    public Optional<ISession> get(ServerUser user) {
        return Optional.ofNullable(map.get(user));
    }

    public Optional<ServerUser> get(ISession session) {
        Optional<Map.Entry<ServerUser, ISession>> optionalEntry = map.entrySet().stream().filter(e -> e.getValue().equals(session)).findFirst();
        return optionalEntry.map(Map.Entry::getKey);
    }

    public Collection<ISession> getFor(Collection<ServerUser> users) {
        return users.stream()
                .map(map::get)
                .filter(Objects::nonNull)
                .toList();
    }

    public Collection<ISession> getAllSessions() {
        return map.values();
    }

    public boolean contains(ISession session) {
        return map.containsValue(session);
    }

    public void remove(ServerUser user) {
        map.remove(user);
    }

    public void remove(ISession session) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

}
