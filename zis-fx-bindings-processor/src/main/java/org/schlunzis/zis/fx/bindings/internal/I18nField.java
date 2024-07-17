package org.schlunzis.zis.fx.bindings.internal;

public abstract class I18nField {

    protected String name;
    protected String key;

    protected String controllerName;
    protected String bindingFactoryName;

    protected I18nField(String name, String key, String controllerName, String bindingFactoryName) {
        this.name = name;
        this.key = key;
        this.controllerName = controllerName;
        this.bindingFactoryName = bindingFactoryName;
    }

    public abstract String createBinding();

}
