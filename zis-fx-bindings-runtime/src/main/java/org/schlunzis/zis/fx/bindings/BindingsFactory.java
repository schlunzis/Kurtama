package org.schlunzis.zis.fx.bindings;

import javafx.beans.binding.StringBinding;

public interface BindingsFactory {

    StringBinding createBinding(String key, final Object... args);

}
