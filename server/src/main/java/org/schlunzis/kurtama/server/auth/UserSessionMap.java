package org.schlunzis.kurtama.server.auth;

import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
class UserSessionMap {

    private final Map<ServerUser, ISession> map = new HashMap<>();

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final ReentrantReadWriteLock.ReadLock readLock = lock.readLock();
    private final ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();

    public void put(ServerUser user, ISession session) {
        Objects.requireNonNull(user);
        Objects.requireNonNull(session);
        writeLock.lock();
        try {
            map.put(user, session);
        } finally {
            writeLock.unlock();
        }
    }

    public Optional<ISession> get(ServerUser user) {
        Objects.requireNonNull(user);
        readLock.lock();
        try {
            return Optional.ofNullable(map.get(user));
        } finally {
            readLock.unlock();
        }
    }

    public Optional<ServerUser> get(ISession session) {
        Objects.requireNonNull(session);
        readLock.lock();
        try {
            Optional<Map.Entry<ServerUser, ISession>> optionalEntry = map.entrySet().stream().filter(e -> e.getValue().equals(session)).findFirst();
            return optionalEntry.map(Map.Entry::getKey);
        } finally {
            readLock.unlock();
        }
    }

    public Collection<ISession> getFor(Collection<ServerUser> users) {
        Objects.requireNonNull(users);
        readLock.lock();
        try {
            return users.stream()
                    .map(map::get)
                    .filter(Objects::nonNull)
                    .toList();
        } finally {
            readLock.unlock();
        }
    }

    public Collection<ISession> getAllSessions() {
        readLock.lock();
        try {
            return map.values();
        } finally {
            readLock.unlock();
        }
    }

    public boolean contains(ISession session) {
        Objects.requireNonNull(session);
        readLock.lock();
        try {
            return map.containsValue(session);
        } finally {
            readLock.unlock();
        }
    }

    public void remove(ServerUser user) {
        Objects.requireNonNull(user);
        writeLock.lock();
        try {
            map.remove(user);
        } finally {
            writeLock.unlock();
        }
    }

    public void remove(ISession session) {
        Objects.requireNonNull(session);
        writeLock.lock();
        try {
            map.entrySet().removeIf(entry -> entry.getValue().equals(session));
        } finally {
            writeLock.unlock();
        }
    }

}
