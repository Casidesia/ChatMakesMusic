package com.casidesia.chatmakesmusic.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

public class LogFactory {
    private static final String LOG_DIRECTORY = "output";
    private static final String LOG_FILE_NAME = LOG_DIRECTORY + File.separator + "ChatMakesMusic.log";

    private LogFactory() {}

    public static <T> Logger getLoggerForClass(Class<T> clazz) {
        try {
            Files.createDirectory(Path.of(LOG_DIRECTORY));
            Logger logger = Logger.getLogger(clazz.getName());
            logger.addHandler(new FileHandler(LOG_FILE_NAME));
            return logger;
        } catch (IOException e) {
            throw new IllegalStateException("Error creating logger for class " + clazz, e);
        }
    }
}
