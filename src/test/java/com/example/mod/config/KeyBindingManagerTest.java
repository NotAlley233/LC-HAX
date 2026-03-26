package com.example.mod.config;

import com.example.mod.module.KeyBindingManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class KeyBindingManagerTest {
    private KeyBindingManager manager;

    @BeforeEach
    void setUp() {
        manager = new KeyBindingManager();
    }

    @Test
    void testBindAndUnbind() {
        assertTrue(manager.bind("autoclicker", 19, false));
        assertEquals(19, manager.getBoundKey("autoclicker"));
        assertEquals("autoclicker", manager.getBoundModule(19));

        assertTrue(manager.unbind("autoclicker"));
        assertNull(manager.getBoundKey("autoclicker"));
        assertNull(manager.getBoundModule(19));
    }

    @Test
    void testConflictDetection() {
        assertTrue(manager.bind("autoclicker", 19, false));
        
        // Try binding another module to the same key without force
        assertFalse(manager.bind("antibot", 19, false));
        assertNull(manager.getBoundKey("antibot"));
        assertEquals("autoclicker", manager.getBoundModule(19));

        // Try binding with force
        assertTrue(manager.bind("antibot", 19, true));
        assertEquals(19, manager.getBoundKey("antibot"));
        assertEquals("antibot", manager.getBoundModule(19));
        assertNull(manager.getBoundKey("autoclicker")); // autoclicker should be unbound
    }

    @Test
    void testRebindSameModule() {
        manager.bind("autoclicker", 19, false);
        manager.bind("autoclicker", 20, false);

        assertEquals(20, manager.getBoundKey("autoclicker"));
        assertEquals("autoclicker", manager.getBoundModule(20));
        assertNull(manager.getBoundModule(19));
    }
}
