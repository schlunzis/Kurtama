package org.schlunzis.kurtama.server.chat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.server.user.ServerUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.UUID;

@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class Chat {

    protected final UUID id;
    protected final Collection<ServerUser> chatters = new ArrayList<>();

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

}
