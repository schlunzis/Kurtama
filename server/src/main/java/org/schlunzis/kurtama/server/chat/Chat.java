package org.schlunzis.kurtama.server.chat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Chat {

    @Getter
    private final UUID id;
    private final Collection<ServerUser> chatters = new ArrayList<>();

    public void addChatter(ServerUser user) {
        Objects.requireNonNull(user);
        if (!chatters.contains(user)) {
            chatters.add(user);
        }
    }

    public void removeChatter(ServerUser user) {
        Objects.requireNonNull(user);
        chatters.remove(user);
    }

    /**
     * Returns all users in this chat. The reference to the internal list is lost.
     *
     * @return the list of users
     */
    public Collection<ServerUser> getChatters() {
        return new ArrayList<>(chatters);
    }

}
