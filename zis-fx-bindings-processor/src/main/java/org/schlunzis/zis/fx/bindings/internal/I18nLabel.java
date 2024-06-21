package org.schlunzis.zis.fx.bindings.internal;

public class I18nLabel extends I18nField {

    public I18nLabel(String name, String key, String controllerName, String bindingFactoryName) {
        super(name, key, controllerName, bindingFactoryName);
    }

    @Override
    public String createBinding() {
        return controllerName + "." + name + ".textProperty().bind(" + bindingFactoryName + ".createBinding(\"" + key + "\"));";
    }

}
