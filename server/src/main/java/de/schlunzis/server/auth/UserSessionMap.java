package de.schlunzis.server.auth;

import de.schlunzis.common.User;
import de.schlunzis.server.net.Session;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class UserSessionMap {

    private final Map<User, Session> userSessionMap = new HashMap<>();

    public void put(User user, Session session) {
        userSessionMap.put(user, session);
    }

    public Session get(User user) {
        return userSessionMap.get(user);
    }

    public Collection<Session> getAllSessions() {
        return userSessionMap.values();
    }

    public boolean contains(Session session) {
        return userSessionMap.containsValue(session);
    }

    public void remove(User user) {
        userSessionMap.remove(user);
    }

    public void remove(Session session) {
        userSessionMap.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

}
