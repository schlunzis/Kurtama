package org.schlunzis.kurtama.client.util;

import org.springframework.stereotype.Component;

@Component
public class VersionManager {

    private static final String DEFAULT_VERSION = "0.0.1-alpha+20240808121326";

    public String getVersion() {
        String version = getClass().getPackage().getImplementationVersion();
        if (version == null || version.isEmpty())
            version = DEFAULT_VERSION;
        return version;
    }

}
