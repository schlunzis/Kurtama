package de.schlunzis.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements User {

    private UUID uuid;

    private String username;

    private String email;

}
