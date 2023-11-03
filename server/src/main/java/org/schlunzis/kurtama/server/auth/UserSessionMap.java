package org.schlunzis.kurtama.server.auth;

import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class UserSessionMap {

    private final Map<ServerUser, ISession> map = new HashMap<>();

    public void put(ServerUser user, ISession session) {
        map.put(user, session);
    }

    public ISession get(ServerUser user) {
        return map.get(user);
    }

    public Optional<ServerUser> get(ISession session) {
        Optional<Map.Entry<ServerUser, ISession>> optionalUser = map.entrySet().stream().filter(e -> e.getValue().equals(session)).findFirst();
        return optionalUser.map(Map.Entry::getKey);
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
