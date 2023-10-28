package de.schlunzis.server.auth;

import de.schlunzis.server.net.Session;
import de.schlunzis.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserSessionMap {

    private final Map<ServerUser, Session> map = new HashMap<>();

    public void put(ServerUser user, Session session) {
        map.put(user, session);
    }

    public Session get(ServerUser user) {
        return map.get(user);
    }

    public Optional<ServerUser> get(Session session) {
        Optional<Map.Entry<ServerUser, Session>> optionalUser = map.entrySet().stream().filter(e -> e.getValue().equals(session)).findFirst();
        return optionalUser.map(Map.Entry::getKey);
    }

    public Collection<Session> getAllSessions() {
        return map.values();
    }

    public boolean contains(Session session) {
        return map.containsValue(session);
    }

    public void remove(ServerUser user) {
        map.remove(user);
    }

    public void remove(Session session) {
        map.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

}
