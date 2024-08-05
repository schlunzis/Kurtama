package org.schlunzis.kurtama.server.chat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Chat {

    @Getter
    private final UUID id;
    private final Collection<ServerUser> chatters = new ArrayList<>();

    void addChatter(@NonNull ServerUser user) {
        if (!chatters.contains(user)) {
            chatters.add(user);
        }
    }

    void removeChatter(@NonNull ServerUser user) {
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
