package org.schlunzis.zis.fx.bindings.internal;

public class I18nButton extends I18nField {

    public I18nButton(String name, String key, String controllerName, String bindingFactoryName) {
        super(name, key, controllerName, bindingFactoryName);
    }

    @Override
    public String createBinding() {
        return "((Button) fieldMap.get(\"" + name + "\").get(c)).textProperty().bind(" + bindingFactoryName + ".createBinding(\"" + key + "\"));";
    }

}
