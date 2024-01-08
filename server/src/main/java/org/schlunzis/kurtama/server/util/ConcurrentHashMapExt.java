package org.schlunzis.kurtama.server.util;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;

public class ConcurrentHashMapExt<K, V> extends ConcurrentHashMap<K, V> {

    public void removeIfValue(Predicate<Entry<K, V>> condition) {
        this.forEachEntry(1, entry -> {
            if (condition.test(entry)) {
                this.remove(entry.getKey());
            }
        });
    }

}
