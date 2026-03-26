package com.example.mod.core;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ModLogger {
    private static final Logger LOGGER = LogManager.getLogger("ExampleMod");
    private static boolean verbose = false;
    private static Path logFile = null;
    private static final ExecutorService logExecutor = Executors.newSingleThreadExecutor(r -> {
        Thread t = new Thread(r, "ModLogger-Thread");
        t.setDaemon(true);
        return t;
    });

    public static void init(Path configDir) {
        Path logDir = configDir.resolve("logs");
        try {
            Files.createDirectories(logDir);
            String dateStr = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            logFile = logDir.resolve("bind-" + dateStr + ".log");
            if (!Files.exists(logFile)) {
                Files.createFile(logFile);
            }
        } catch (IOException e) {
            LOGGER.error("Failed to initialize file logger", e);
        }
    }

    public static void setVerbose(boolean isVerbose) {
        verbose = isVerbose;
    }

    private static void writeToFile(String level, String message, Throwable t) {
        if (logFile == null) return;
        
        logExecutor.submit(() -> {
            try (BufferedWriter writer = Files.newBufferedWriter(logFile, StandardOpenOption.APPEND)) {
                String timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
                writer.write(String.format("[%s] [%s] %s%n", timestamp, level, message));
                if (t != null) {
                    writer.write(t.toString() + "\n");
                    for (StackTraceElement element : t.getStackTrace()) {
                        writer.write("\tat " + element.toString() + "\n");
                    }
                }
            } catch (IOException e) {
                LOGGER.error("Failed to write to log file", e);
            }
        });
    }

    public static void info(String message) {
        LOGGER.info(message);
        writeToFile("INFO", message, null);
    }

    public static void warn(String message) {
        LOGGER.warn(message);
        writeToFile("WARN", message, null);
    }

    public static void error(String message) {
        LOGGER.error(message);
        writeToFile("ERROR", message, null);
    }

    public static void error(String message, Throwable t) {
        LOGGER.error(message, t);
        writeToFile("ERROR", message, t);
    }

    public static void debug(String message) {
        if (verbose) {
            LOGGER.info("[DEBUG] " + message);
            writeToFile("DEBUG", message, null);
        }
    }
}
