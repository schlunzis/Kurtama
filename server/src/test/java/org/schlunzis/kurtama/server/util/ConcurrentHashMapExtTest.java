package org.schlunzis.kurtama.server.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ConcurrentHashMapExtTest {

    ConcurrentHashMapExt<String, String> map;

    @BeforeEach
    void init() {
        map = new ConcurrentHashMapExt<>();
    }

    @Test
    void removeIfValueTest() {
        map.put("one", "test");
        map.put("two", "test2");

        map.removeIfValue(entry -> entry.getValue().equals("test2"));

        assertEquals(1, map.size());
        assertEquals("test", map.get("one"));
    }

}
