package de.schlunzis.common;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import de.schlunzis.common.util.InheritanceTypeIdResolver;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "discriminator")
@JsonTypeIdResolver(InheritanceTypeIdResolver.class)
public interface User {

    String getUsername();

    void setUsername(String username);

    String getEmail();

}
