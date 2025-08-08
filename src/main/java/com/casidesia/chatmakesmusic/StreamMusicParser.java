package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.ParseResult;
import com.casidesia.chatmakesmusic.data.ParsedNote;
import com.casidesia.chatmakesmusic.data.ParsedNoteOrRest;
import com.casidesia.chatmakesmusic.data.ParsedRest;
import com.casidesia.chatmakesmusic.enums.NoteLength;
import com.casidesia.chatmakesmusic.util.LogFactory;
import org.audiveris.proxymusic.Step;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StreamMusicParser {
    private static final Logger log = LogFactory.getLoggerForClass(StreamMusicParser.class);

    // Parsing-related constants
    public static class Constants {
        private static final List<String> STEPS = Arrays.stream(Step.values()).map(Step::name).toList();
        private static final String REST = "rest";
        private static final String TIME_SIGNATURE_DELIMITER = "/";
        private static final String LINE_DELIMITER = ":";

        private static final int DEFAULT_TIME_UPPER = 4;
        private static final int DEFAULT_TIME_LOWER = 4;
        private static final int DEFAULT_OCTAVE = 4;
    }

    private final String inputFilename;
    private int timeSignatureUpper = Constants.DEFAULT_TIME_UPPER;
    private int timeSignatureLower = Constants.DEFAULT_TIME_LOWER;
    private int currentOctave = Constants.DEFAULT_OCTAVE;
    private final List<ParsedNoteOrRest> notes = new ArrayList<>();

    public StreamMusicParser(String inputFilename) {
        this.inputFilename = inputFilename;
    }

    public ParseResult parseFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            reader.lines().forEach(this::parseLine);
        }
        return new ParseResult(timeSignatureUpper, timeSignatureLower, notes);
    }

    private void parseLine(String line) {
        String[] tokens = line.split(Constants.LINE_DELIMITER);
        switch (tokens[0]) {
            case "Clock" -> {} // TODO: Implement or remove
            case "time" -> parseTime(tokens[1]);
            case "Octave" -> parseOctave(tokens[1]);
            default -> {
                // Check if note
                NoteLength noteLength = NoteLength.tryParse(tokens[0]);
                if (noteLength == null)
                    logAndThrowIllegalArgument("Could not parse line: " + line);
                parseNote(noteLength, tokens[1]);
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                 INDIVIDUAL PARSING METHODS                                                     //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void parseTime(String timeSignatureString) {
        String[] tokens = timeSignatureString.split(Constants.TIME_SIGNATURE_DELIMITER);
        if (tokens.length != 2)
            logAndThrowIllegalArgument("Invalid time signature: " + timeSignatureString);
        timeSignatureLower = tryParseInt(tokens[0], "timeSignatureLower");
        timeSignatureUpper = tryParseInt(tokens[0], "timeSignatureUpper");
    }

    private void parseOctave(String octaveString) {
        currentOctave = tryParseInt(octaveString, "Octave");
    }

    private int tryParseInt(String value, String fieldName) {
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logAndThrowIllegalArgument("Invalid value given for field " + fieldName + ": " + value);
        }
        return 0; // Cannot happen; IllegalArgumentException will be thrown above
    }

    private void parseNote(NoteLength noteLength, String pitchOrRestString) {
        if (Constants.REST.equals(pitchOrRestString))
            notes.add(new ParsedRest(noteLength));
        else if (Constants.STEPS.contains(pitchOrRestString))
            notes.add(new ParsedNote(noteLength, Step.fromValue(pitchOrRestString), currentOctave));
        else
            logAndThrowIllegalArgument("Invalid note: " + pitchOrRestString);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                 EXCEPTION HANDLING                                                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void logAndThrowIllegalArgument(String logString) {
        IllegalArgumentException e = new IllegalArgumentException(logString);
        log.log(Level.SEVERE, "Error parsing input file: " + inputFilename, e);
        throw e;
    }
}
