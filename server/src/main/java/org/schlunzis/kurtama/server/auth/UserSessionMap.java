package org.schlunzis.kurtama.server.auth;

import lombok.NonNull;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.schlunzis.zis.commons.collections.BiMap;
import org.schlunzis.zis.commons.collections.ConcurrentBiHashMap;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
class UserSessionMap {

    private final BiMap<ServerUser, ISession> map = new ConcurrentBiHashMap<>();

    public void put(@NonNull ServerUser user, @NonNull ISession session) {
        map.put(user, session);
    }

    public Optional<ISession> get(@NonNull ServerUser user) {
        return Optional.ofNullable(map.get(user));
    }

    public Optional<ServerUser> get(@NonNull ISession session) {
        return Optional.ofNullable(map.getByValue(session));
    }

    public Collection<ISession> getFor(@NonNull Collection<ServerUser> users) {
        return map.getForKeys(users);
    }

    public Collection<ISession> getAllSessions() {
        return map.values();
    }

    public boolean contains(@NonNull ISession session) {
        return map.containsValue(session);
    }

    public void remove(@NonNull ServerUser user) {
        map.remove(user);
    }

    public void remove(@NonNull ISession session) {
        map.removeByValue(session);
    }

}
