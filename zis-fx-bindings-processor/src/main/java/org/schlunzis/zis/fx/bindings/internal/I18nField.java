package org.schlunzis.zis.fx.bindings.internal;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class I18nField {

    protected String name;
    protected String key;

    protected String controllerName;
    protected String bindingFactoryName;

    public abstract String createBinding();

}
