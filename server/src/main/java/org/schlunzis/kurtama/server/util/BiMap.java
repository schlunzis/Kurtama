package org.schlunzis.kurtama.server.util;

import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Provides helper methods to bidirectional access for maps.
 *
 * @param <K> key class
 * @param <V> value class
 */
public interface BiMap<K, V> extends Map<K, V> {

    /**
     * Returns the first key mapping to the given value.
     *
     * @param value the value to search for
     * @return the key or null, if no entry was found
     */
    default K getByValue(V value) {
        return entrySet().stream()
                .filter(e -> {
                    if (e.getValue() == null) return value == null;
                    else return e.getValue().equals(value);
                })
                .map(Entry::getKey)
                .findFirst().orElse(null);
    }

    /**
     * Removes all entries with the given value.
     *
     * @param value the value to search for
     * @return the key of the last removed entry
     */
    default K removeByValue(V value) {
        final AtomicReference<K> prev = new AtomicReference<>(null);
        entrySet().removeIf(e -> {
            if ((e.getValue() == null && value == null) || (e.getValue() != null && e.getValue().equals(value))) {
                prev.setPlain(e.getKey());
                return true;
            }
            return false;
        });
        return prev.getPlain();
    }

    /**
     * Looks for all the given keys and returns all values found for the keys. The order is not preserved and null
     * entries are removed.
     *
     * @param keys the keys to look for
     * @return all values found for the given keys
     */
    default Collection<V> getForKeys(Collection<K> keys) {
        Objects.requireNonNull(keys);
        return keys.stream()
                .map(this::get)
                .filter(Objects::nonNull)
                .toList();
    }

    /**
     * Looks for all the given values and returns all keys found for the values. The order is not preserved and null
     * entries are removed.
     *
     * @param values the values to look for
     * @return all keys found for the given values
     */
    default Collection<K> getForValues(Collection<V> values) {
        Objects.requireNonNull(values);
        return values.stream()
                .map(this::getByValue)
                .filter(Objects::nonNull)
                .toList();
    }

}
