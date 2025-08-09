package com.casidesia.chatmakesmusic;

import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.util.Marshalling;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ChatsSongPrinter {
    private static final Logger log = LoggerFactory.getLogger(ChatsSongPrinter.class);

    private final String outputFilename;

    public ChatsSongPrinter(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public void printScoreToFile(ScorePartwise score) throws Marshalling.MarshallingException, IOException {
        try (OutputStream os = new FileOutputStream(outputFilename)) {
            Marshalling.marshal(score, os, true, 2);
        } catch (IOException | Marshalling.MarshallingException e) {
            log.error("Error writing score to file {}", outputFilename, e);
            throw e;
        }
    }
}
