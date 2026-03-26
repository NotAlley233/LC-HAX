package com.example.mod.config;

import com.example.mod.module.KeyBindingManager;
import com.example.mod.module.ModuleManager;
import com.example.mod.property.PropertyManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class ConfigManagerTest {
    private ConfigManager configManager;
    private KeyBindingManager keyBindingManager;
    private ModuleManager moduleManager;
    private PropertyManager propertyManager;
    private Path tempDir;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("test-mod-config");
        keyBindingManager = new KeyBindingManager();
        moduleManager = new ModuleManager();
        propertyManager = new PropertyManager();
        configManager = new ConfigManager(tempDir, moduleManager, propertyManager, keyBindingManager);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (Files.exists(tempDir)) {
            Files.walk(tempDir)
                 .sorted(Comparator.reverseOrder())
                 .forEach(p -> {
                     try { Files.delete(p); } catch (IOException ignored) {}
                 });
        }
    }

    @Test
    void testSaveAndLoadProfile() throws Exception {
        keyBindingManager.bind("autoclicker", 19, true);
        configManager.save("profileA").get(); // Wait for completion

        // Clear memory
        keyBindingManager.clear();
        assertNull(keyBindingManager.getBoundKey("autoclicker"));

        // Load profile
        configManager.load("profileA").get();
        assertEquals(19, keyBindingManager.getBoundKey("autoclicker"));
    }

    @Test
    void testRollbackOnCorruptedJson() throws Exception {
        keyBindingManager.bind("autoclicker", 19, true);
        configManager.save("profileB").get();

        // Corrupt the JSON file
        Path profilePath = tempDir.resolve("keymaps/profileB.json");
        Files.write(profilePath, "{ \"corrupted\": true ".getBytes());

        // Try to load, should fail and rollback/not change memory
        keyBindingManager.bind("antibot", 20, true);
        
        try {
            configManager.load("profileB").get();
            fail("Should throw exception on invalid JSON");
        } catch (Exception e) {
            // Expected
        }

        // Memory should remain intact
        assertEquals(20, keyBindingManager.getBoundKey("antibot"));
        assertEquals(19, keyBindingManager.getBoundKey("autoclicker"));
    }

    @Test
    void testPerformance1000Bindings() throws Exception {
        for (int i = 0; i < 1000; i++) {
            keyBindingManager.bind("module" + i, i, true);
        }
        configManager.save("perfTest").get();

        keyBindingManager.clear();

        long startTime = System.nanoTime();
        configManager.load("perfTest").get();
        long endTime = System.nanoTime();

        long durationMs = (endTime - startTime) / 1_000_000;
        assertTrue(durationMs < 50, "Loading 1000 bindings took " + durationMs + "ms, which is >= 50ms");
        assertEquals(1000, keyBindingManager.getAllBindings().size());
    }

    @Test
    void testConcurrentLoading() throws Exception {
        keyBindingManager.bind("test", 1, true);
        configManager.save("concurrent1").get();
        keyBindingManager.bind("test", 2, true);
        configManager.save("concurrent2").get();

        int threadCount = 10;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            int index = i;
            executor.submit(() -> {
                try {
                    configManager.load(index % 2 == 0 ? "concurrent1" : "concurrent2").get();
                } catch (Exception e) {
                    fail("Concurrent load failed");
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();
        
        // As long as no exception was thrown and state is consistent with one of the profiles, it passes.
        Integer boundKey = keyBindingManager.getBoundKey("test");
        assertTrue(boundKey == 1 || boundKey == 2);
    }
}
