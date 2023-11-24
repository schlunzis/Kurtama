package org.schlunzis.kurtama.server.auth;

import org.schlunzis.kurtama.server.net.ISession;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.Collection;
import java.util.Optional;

public interface IAuthenticationService {

    /**
     * Returns true, if a user with the given session is logged in.
     *
     * @param session the session to check
     * @return true, if a user is logged in, false otherwise
     */
    boolean isLoggedIn(ISession session);

    /**
     * Returns the sessions of all logged-in users.
     *
     * @return the sessions
     */
    Collection<ISession> getAllLoggedInSessions();

    /**
     * Returns the user associated with the session, if one exists. There might not be a user for that session, if they
     * are not logged in.
     *
     * @param session the session to check for
     * @return the user, if they are logged in
     */
    Optional<ServerUser> getUserForSession(ISession session);

    /**
     * This method returns sessions for logged-in users. The order of the sessions is not defined. If the amount of
     * sessions returned is not equal to the amount of users given, then some of the users were not logged in.
     *
     * @param users the users to get the sessions for
     * @return the sessions
     */
    Collection<ISession> getSessionsForUsers(Collection<ServerUser> users);

    /**
     * Returns the session for the given user. If the session is not present, the user is not logged-in.
     *
     * @param user the user
     * @return the session
     */
    Optional<ISession> getSessionForUser(ServerUser user);

}
