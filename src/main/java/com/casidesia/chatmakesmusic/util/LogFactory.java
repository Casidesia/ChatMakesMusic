package com.casidesia.chatmakesmusic.util;

import com.casidesia.chatmakesmusic.ChatMakesMusic;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

// TODO: Look into log4j
public class LogFactory {
    private static final String LOG_DIRECTORY = "output";
    private static final String LOG_FILE_NAME = LOG_DIRECTORY + File.separator + "ChatMakesMusic.log";
    private static Logger log;

    private LogFactory() {}

    public static Logger getLogger() {
        if (log == null) {
            try {
                Files.createDirectories(Path.of(LOG_DIRECTORY));
                log = Logger.getLogger(ChatMakesMusic.class.getName());
                FileHandler handler = new FileHandler(LOG_FILE_NAME);
                handler.setFormatter(new SimpleFormatter());
                log.addHandler(handler);
            } catch (IOException e) {
                throw new IllegalStateException("Error creating logger", e);
            }
        }
        return log;
    }
}
