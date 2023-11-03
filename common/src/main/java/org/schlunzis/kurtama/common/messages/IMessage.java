package org.schlunzis.kurtama.common.messages;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.schlunzis.kurtama.common.util.InheritanceTypeIdResolver;

//@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "discriminator")
@JsonTypeIdResolver(InheritanceTypeIdResolver.class)
public interface IMessage {

}
