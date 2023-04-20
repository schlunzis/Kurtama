package de.schlunzis.common.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import de.schlunzis.common.util.InheritanceTypeIdResolver;

//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "discriminator")
@JsonTypeIdResolver(InheritanceTypeIdResolver.class)
public interface IMessage {

}
