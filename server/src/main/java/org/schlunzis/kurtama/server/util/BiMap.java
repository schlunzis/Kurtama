package org.schlunzis.kurtama.server.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public interface BiMap<K, V> extends Map<K, V> {

    default K getByValue(V value) {
        return entrySet().stream()
                .filter(e -> e.getValue().equals(value))
                .map(Entry::getKey)
                .findFirst().orElse(null);
    }

    default K removeByValue(V value) {
        final AtomicReference<K> prev = new AtomicReference<>(null);
        entrySet().removeIf(e -> {
            if (e.getValue().equals(value)) {
                prev.setPlain(e.getKey());
                return true;
            }
            return false;
        });
        return prev.getPlain();
    }

    default Collection<V> getForKeys(Collection<K> keys) {
        Objects.requireNonNull(keys);
        return keys.stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .toList();
    }

    default Collection<K> getForValues(Collection<V> values) {
        Objects.requireNonNull(values);
        return values.stream()
                .map(this::getByValue)
                .filter(Objects::nonNull)
                .toList();
    }

}
