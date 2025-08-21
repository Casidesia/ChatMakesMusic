package com.casidesia.chatmakesmusic.datagenerator;

import com.casidesia.chatmakesmusic.enums.Instrument;
import com.casidesia.chatmakesmusic.enums.Note;
import com.casidesia.chatmakesmusic.enums.NoteLength;
import com.casidesia.chatmakesmusic.enums.TimeSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

public class DataGenerator {
    private static final Logger log = LoggerFactory.getLogger(DataGenerator.class);
    private static String outputFilename = "output/testData" + System.currentTimeMillis() + ".txt";

    public DataGenerator(String outputFilename) {
        DataGenerator.outputFilename = outputFilename;
    }

    public static void main(String[] args) throws IOException {
        Files.createDirectories(Path.of("output"));
        Files.createFile(Path.of(outputFilename));

        OutputStream os = new FileOutputStream(outputFilename);
        PrintWriter writer = new PrintWriter(os, true);

        writer.println("time:" + TimeSignature.randomTimeSignature());
        writer.println("Instrument:" + Instrument.randomInstrument().getInstrumentName());
        for (int i = 0; i <= 70; i++) {
            Random rand = new Random();
            int choice = rand.nextInt(1, 21);
            if (choice == 1)
                writer.println("Octave:" + rand.nextInt(3, 7));
            else {
                int restOrNote = rand.nextInt(1, 21);
                if (restOrNote == 1) {
                    writer.println("quarter:rest");
                } else {
                    String length = NoteLength.randomLength();
                    String note = Note.randomNote();
                    writer.println(length + ":" + note);

                }
            }
        }
        writer.flush();
        writer.close();
    }
}
