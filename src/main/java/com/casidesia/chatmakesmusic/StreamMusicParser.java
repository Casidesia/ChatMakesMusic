package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.ParsedNote;
import com.casidesia.chatmakesmusic.data.ParsedRest;
import com.casidesia.chatmakesmusic.enums.Instrument;
import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.ScoreInstrument;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.Step;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class StreamMusicParser {
    private static final Logger log = LoggerFactory.getLogger(StreamMusicParser.class);

    // Parsing-related constants
    private static class Constants {
        private static final List<String> STEPS = Arrays.stream(Step.values()).map(Step::name).toList();
        private static final String REST = "rest";
        private static final String TIME_SIGNATURE_DELIMITER = "/";
        private static final String LINE_DELIMITER = ":";

        private static final int DEFAULT_OCTAVE = 4;
    }


    private final ScoreBuilder scoreBuilder;
    private final String inputFilename;
    private int currentOctave = Constants.DEFAULT_OCTAVE;
    private String instrument = "Piano";

    public StreamMusicParser(String inputFilename) {
        scoreBuilder = new ScoreBuilder();
        this.inputFilename = inputFilename;
    }

    public ScorePartwise parseFile() throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            reader.lines().forEach(this::parseLine);
        }
        return scoreBuilder.getScore();
    }

    private void parseLine(String line) {
        String[] tokens = line.split(Constants.LINE_DELIMITER);
        switch (tokens[0]) {
            case "Clock" -> {} // TODO: Implement or remove
            case "time" -> parseTime(tokens[1]);
            case "Octave" -> parseOctave(tokens[1]);
            case "Instrument" -> parseInstrument(tokens[1]);
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
        log.info("Current tokens for parsed time signature string {}", Arrays.stream(tokens).toList());

        if (tokens.length != 2)
            logAndThrowIllegalArgument("Invalid time signature: " + timeSignatureString);

        int timeSignatureUpper = tryParseInt(tokens[0], "timeSignatureUpper");
        log.info("Time signature upper variable set to: {}", timeSignatureUpper);

        int timeSignatureLower = tryParseInt(tokens[1], "timeSignatureLower");
        log.info("Time signature lower variable set to: {}", timeSignatureLower);

        scoreBuilder.setTimeSignature(timeSignatureUpper, timeSignatureLower);
    }

    private void parseOctave(String octaveString) {
        currentOctave = tryParseInt(octaveString, "Octave");
        log.info("Current octave variable set to: {}", currentOctave);
    }
    private void parseInstrument(String instrumentString) {
        Instrument instrument = Instrument.tryParse(instrumentString);
        scoreBuilder.setInstrument(instrument);
    }

    private int tryParseInt(String value, String fieldName) {
        log.info("Parsing:: field name: {}, value: {}", fieldName, value);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            logAndThrowIllegalArgument("Invalid value given for field " + fieldName + ": " + value);
        }
        return 0; // Cannot happen; IllegalArgumentException will be thrown above
    }

    private void parseNote(NoteLength noteLength, String pitchOrRestString) {
        log.info("Parsing note: Note length: {}, pitch or rest string: {}", noteLength, pitchOrRestString);
        if (Constants.REST.equals(pitchOrRestString)) {
            ParsedRest parsedRest = new ParsedRest(noteLength);
            scoreBuilder.addNote(parsedRest);
            log.info("Rest added: {}", parsedRest.getDuration());
        }
        else if (Constants.STEPS.contains(pitchOrRestString)) {
            ParsedNote parsedNote = new ParsedNote(noteLength, Step.fromValue(pitchOrRestString), currentOctave);
            scoreBuilder.addNote(parsedNote);
            log.info("Note added: {}", parsedNote);
        }
        else
            logAndThrowIllegalArgument("Invalid note: " + pitchOrRestString);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                 EXCEPTION HANDLING                                                             //
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void logAndThrowIllegalArgument(String logString) {
        IllegalArgumentException e = new IllegalArgumentException(logString);
        log.error("Error parsing input file: {}", inputFilename, e);
        throw e;
    }
}
