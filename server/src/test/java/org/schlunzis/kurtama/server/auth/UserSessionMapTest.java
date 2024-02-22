package org.schlunzis.kurtama.server.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserSessionMapTest {

    UserSessionMap userSessionMap;

    @Mock
    ServerUser defaultUser;
    @Mock
    ISession defaultSession;

    @Mock
    ServerUser secondUser;
    @Mock
    ISession secondSession;

    @BeforeEach
    void init() {
        userSessionMap = new UserSessionMap();
    }

    // ################################################
    // put(ServerUser, ISession)
    // ################################################

    @Test
    void putDefaultTest() {
        userSessionMap.put(defaultUser, defaultSession);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isPresent());
        assertEquals(defaultUser, user.get());

        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isPresent());
        assertEquals(defaultSession, session.get());
    }

    @Test
    void putNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.put(null, defaultSession));
        assertThrows(NullPointerException.class,
                () -> userSessionMap.put(defaultUser, null));
        assertThrows(NullPointerException.class,
                () -> userSessionMap.put(null, null));
    }

    @Test
    void putMoreThanOneTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isPresent());
        assertEquals(defaultUser, user.get());
        user = userSessionMap.get(secondSession);
        assertTrue(user.isPresent());
        assertEquals(secondUser, user.get());

        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isPresent());
        assertEquals(defaultSession, session.get());
        session = userSessionMap.get(secondUser);
        assertTrue(session.isPresent());
        assertEquals(secondSession, session.get());
    }

    // ################################################
    // get(ServerUser)
    // ################################################

    @Test
    void getSessionFromUserDefaultTest() {
        userSessionMap.put(defaultUser, defaultSession);

        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isPresent());
        assertEquals(defaultSession, session.get());
    }

    @Test
    void getSessionFromUserEmptyMapTest() {
        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isEmpty());
    }

    @Test
    void getSessionFromUserNonExistentTest() {
        userSessionMap.put(defaultUser, defaultSession);

        Optional<ISession> session = userSessionMap.get(secondUser);
        assertTrue(session.isEmpty());
    }

    @Test
    void getSessionFromUserNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.get((ServerUser) null));
    }

    // ################################################
    // get(ISession)
    // ################################################

    @Test
    void getUserFromSessionDefaultTest() {
        userSessionMap.put(defaultUser, defaultSession);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isPresent());
        assertEquals(defaultUser, user.get());
    }

    @Test
    void getUserFromSessionEmptyMapTest() {
        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isEmpty());
    }

    @Test
    void getUserFromSessionNonExistentTest() {
        userSessionMap.put(defaultUser, defaultSession);

        Optional<ServerUser> user = userSessionMap.get(secondSession);
        assertTrue(user.isEmpty());
    }

    @Test
    void getUserFromSessionNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.get((ISession) null));
    }

    // ################################################
    // getFor(Collection<ServerUser>)
    // ################################################

    @Test
    void getForNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.getFor(null));
    }

    @Test
    void getForEmptyListAndEmptyMapTest() {
        Collection<ServerUser> users = new ArrayList<>();

        Collection<ISession> sessions = userSessionMap.getFor(users);

        assertTrue(sessions.isEmpty());
        assertTrue(users.isEmpty());
    }

    @Test
    void getForEmptyListTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);

        Collection<ServerUser> users = new ArrayList<>();

        Collection<ISession> sessions = userSessionMap.getFor(users);

        assertTrue(sessions.isEmpty());
        assertTrue(users.isEmpty());
    }

    @Test
    void getForEmptyMapTest() {
        Collection<ServerUser> users = new ArrayList<>();

        Collection<ISession> sessions = userSessionMap.getFor(users);
        users.add(defaultUser);
        users.add(secondUser);

        assertTrue(sessions.isEmpty());
        assertEquals(2, users.size());
        assertTrue(users.contains(defaultUser));
        assertTrue(users.contains(secondUser));
    }

    @Test
    void getForEffectivelyAllTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);

        Collection<ServerUser> users = new ArrayList<>(2);
        users.add(defaultUser);
        users.add(secondUser);

        Collection<ISession> sessions = userSessionMap.getFor(users);

        assertEquals(2, sessions.size());
        assertTrue(userSessionMap.contains(defaultSession));
        assertTrue(userSessionMap.contains(secondSession));
        assertEquals(2, users.size());
        assertTrue(users.contains(defaultUser));
        assertTrue(users.contains(secondUser));
    }

    // ################################################
    // getAllSessions()
    // ################################################

    @Test
    void getAllSessionsEmptyTest() {
        Collection<ISession> sessions = userSessionMap.getAllSessions();

        assertTrue(sessions.isEmpty());
    }

    @Test
    void getAllSessionsOneTest() {
        userSessionMap.put(defaultUser, defaultSession);

        Collection<ISession> sessions = userSessionMap.getAllSessions();

        assertEquals(1, sessions.size());
        assertTrue(userSessionMap.contains(defaultSession));
    }

    @Test
    void getAllSessionsTwoTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);

        Collection<ISession> sessions = userSessionMap.getAllSessions();

        assertEquals(2, sessions.size());
        assertTrue(userSessionMap.contains(defaultSession));
        assertTrue(userSessionMap.contains(secondSession));
    }

    // ################################################
    // contains(ISession)
    // ################################################

    @Test
    void containsNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.contains(null));
    }

    @Test
    void containsEmptyTest() {
        assertFalse(userSessionMap.contains(defaultSession));
    }

    @Test
    void containsOneTest() {
        userSessionMap.put(defaultUser, defaultSession);

        assertTrue(userSessionMap.contains(defaultSession));
    }

    @Test
    void containsTwoTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);


        assertTrue(userSessionMap.contains(secondSession));
        assertTrue(userSessionMap.contains(defaultSession));
    }

    @Test
    void containsNotTest() {
        userSessionMap.put(defaultUser, defaultSession);


        assertFalse(userSessionMap.contains(secondSession));
        assertTrue(userSessionMap.contains(defaultSession));
    }

    // ################################################
    // remove(ServerUser)
    // ################################################

    @Test
    void removeUserNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.remove((ServerUser) null));
    }

    @Test
    void removeUserDefaultTest() {
        userSessionMap.put(defaultUser, defaultSession);

        userSessionMap.remove(defaultUser);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isEmpty());

        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isEmpty());
    }

    @Test
    void removeUserDefaultTwoUsersTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);

        userSessionMap.remove(defaultUser);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isEmpty());
        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isEmpty());

        user = userSessionMap.get(secondSession);
        assertTrue(user.isPresent());
        assertEquals(secondUser, user.get());
        session = userSessionMap.get(secondUser);
        assertTrue(session.isPresent());
        assertEquals(secondSession, session.get());
    }

    // ################################################
    // remove(ISession)
    // ################################################

    @Test
    void removeSessionNullTest() {
        assertThrows(NullPointerException.class,
                () -> userSessionMap.remove((ISession) null));
    }

    @Test
    void removeSessionDefaultTest() {
        userSessionMap.put(defaultUser, defaultSession);

        userSessionMap.remove(defaultSession);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isEmpty());

        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isEmpty());
    }

    @Test
    void removeSessionDefaultTwoUsersTest() {
        userSessionMap.put(defaultUser, defaultSession);
        userSessionMap.put(secondUser, secondSession);

        userSessionMap.remove(defaultSession);

        Optional<ServerUser> user = userSessionMap.get(defaultSession);
        assertTrue(user.isEmpty());
        Optional<ISession> session = userSessionMap.get(defaultUser);
        assertTrue(session.isEmpty());

        user = userSessionMap.get(secondSession);
        assertTrue(user.isPresent());
        assertEquals(secondUser, user.get());
        session = userSessionMap.get(secondUser);
        assertTrue(session.isPresent());
        assertEquals(secondSession, session.get());
    }

}
