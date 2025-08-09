package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.util.LogFactory;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.util.Marshalling;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatsSongPrinter {
    private static final Logger log = LogFactory.getLogger();

    private final String outputFilename;

    public ChatsSongPrinter(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    public void printScoreToFile(ScorePartwise score) throws Marshalling.MarshallingException, IOException {
        try (OutputStream os = new FileOutputStream(outputFilename)) {
            Marshalling.marshal(score, os, true, 2);
        } catch (IOException | Marshalling.MarshallingException e) {
            log.log(Level.SEVERE, "Error writing score to file " + outputFilename, e);
            throw e;
        }
    }
}
