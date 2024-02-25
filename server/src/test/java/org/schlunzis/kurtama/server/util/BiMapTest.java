package org.schlunzis.kurtama.server.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class BiMapTest {

    BiMap<String, Object> map;

    String defaultKey = "key";
    String otherKey = "other key";
    String thirdKey = "third key";

    @Mock
    Object defaultObj;
    @Mock
    Object otherObj;
    @Mock
    Object thirdObj;

    @BeforeEach
    void init() {
        map = new BiHashMap<>();
        map.put(defaultKey, defaultObj);
        map.put(otherKey, otherObj);
        map.put(thirdKey, thirdObj);
    }

    // ################################################
    // getByValue(V)
    // ################################################

    @Test
    void getByValueTest() {
        assertNull(map.getByValue(new Object()));

        assertEquals(defaultKey, map.getByValue(defaultObj));
        assertEquals(otherKey, map.getByValue(otherObj));

        map.clear();

        assertNull(map.getByValue(defaultObj));
    }

    @Test
    void getByValueNullTest() {
        assertNull(map.getByValue(null));
        map.put(defaultKey, null);
        assertEquals(defaultKey, map.getByValue(null));
    }

    // ################################################
    // removeByValue(V)
    // ################################################

    @Test
    void removeByValueTest() {
        assertNull(map.removeByValue(new Object()));

        assertEquals(defaultKey, map.removeByValue(defaultObj));
        assertEquals(otherKey, map.removeByValue(otherObj));
        assertEquals(1, map.size());
        assertNull(map.get(defaultKey));
        assertNull(map.get(otherKey));

        map.clear();

        assertNull(map.removeByValue(defaultObj));
        assertNull(map.removeByValue(otherObj));
    }

    @Test
    void removeByValueNullTest() {
        assertNull(map.removeByValue(null));
        assertEquals(3, map.size());
        map.put(defaultKey, null);
        assertEquals(defaultKey, map.removeByValue(null));
    }

    // ################################################
    // getForKeys(Collection<V>)
    // ################################################

    @Test
    void getForKeysNullTest() {
        assertThrows(NullPointerException.class, () -> map.getForKeys(null));
    }

    @Test
    void getForKeysTest() {
        Collection<String> keys = new ArrayList<>();
        Collections.addAll(keys, defaultKey, null, otherKey, "no key");

        Collection<Object> objects = map.getForKeys(keys);

        assertEquals(2, objects.size());
        assertTrue(objects.contains(defaultObj));
        assertTrue(objects.contains(otherObj));
    }

    // ################################################
    // getForValues(Collection<K>)
    // ################################################

    @Test
    void getForValuesNullTest() {
        assertThrows(NullPointerException.class, () -> map.getForValues(null));
    }

    @Test
    void getForValuesTest() {
        Collection<Object> values = new ArrayList<>();
        Collections.addAll(values, defaultObj, null, otherObj, new Object());

        Collection<String> objects = map.getForValues(values);

        assertEquals(2, objects.size());
        assertTrue(objects.contains(defaultKey));
        assertTrue(objects.contains(otherKey));
    }

}
