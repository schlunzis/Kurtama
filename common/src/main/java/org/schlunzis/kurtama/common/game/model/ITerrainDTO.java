package org.schlunzis.kurtama.common.game.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import org.schlunzis.kurtama.common.util.InheritanceTypeIdResolver;

import java.util.List;

@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "discriminator")
@JsonTypeIdResolver(InheritanceTypeIdResolver.class)
public interface ITerrainDTO {

    int width();

    int height();

    ITileDTO[][] tiles();

    List<EdgeDTO> edges();

}
