package org.schlunzis.kurtama.common.game.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.schlunzis.kurtama.common.IUser;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class Team {

    private final UUID id;
    private final Color color;
    private final List<IUser> users = new ArrayList<>();

}
