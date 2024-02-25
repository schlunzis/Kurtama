package org.schlunzis.kurtama.server.util;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentBiHashMap<K, V> extends ConcurrentHashMap<K, V> implements BiMap<K, V> {
}
