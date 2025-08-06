package com.casidesia.chatmakesmusic;

import org.audiveris.proxymusic.*;
import org.audiveris.proxymusic.util.Marshalling;

import java.io.*;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    private static final ObjectFactory factory = new ObjectFactory();
    private static final Attributes attributes = factory.createAttributes();

    static int timeBeats;
    private static int octave = 4;
    private static int totalDuration = 0;
    static int currentMeasureNum = 1;
    static int currentBeatNum = 0;
    static int noteNumber = 0;

    public static void main(String[] args) throws IOException {
        initLogger();
        logger.info("Application successfully started.");

        ScorePartwise score = initXMLFile();
        ScorePartwise.Part part = score.getPart().get(0);
        ScorePartwise.Part.Measure measure = createFirstMeasure();
        part.getMeasure().add(measure);

        String inputFilename = args[0];
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilename))) {
            while (addToXML(reader.readLine(), measure)) ;
        }
        attributes.setDivisions(new BigDecimal(totalDuration * 4));

        marshalScore(score);
    }

    private static void initLogger() throws IOException {
        Files.createDirectories(Path.of("output"));
        FileHandler fh = new FileHandler("output/ChatMakesMusic.log");
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }

    public static ScorePartwise initXMLFile() {
        // <score-partwise>
        ScorePartwise scorePartwise = factory.createScorePartwise();

        // Title
        scorePartwise.setMovementTitle("Chats Song");
        // Composer
        Identification identification = factory.createIdentification();
        TypedText typedText = factory.createTypedText();
        typedText.setValue("The Composer");
        typedText.setType("composer");
        identification.getCreator().add(typedText);
        scorePartwise.setIdentification(identification);

        // PartList
        PartList partList = factory.createPartList();
        scorePartwise.setPartList(partList);

        // ScorePart
        ScorePart scorePart = factory.createScorePart();
        partList.getPartGroupOrScorePart().add(scorePart);
        scorePart.setId("P1");

        PartName partName = factory.createPartName();
        scorePart.setPartName(partName);
        partName.setValue("Music");

        ScorePartwise.Part part = factory.createScorePartwisePart();
        scorePartwise.getPart().add(part);
        part.setId(scorePart);

        return scorePartwise;
    }

    private static ScorePartwise.Part.Measure createFirstMeasure() {
        // First Measure
        ScorePartwise.Part.Measure measure = factory.createScorePartwisePartMeasure();
        measure.setNumber("1");

        measure.getNoteOrBackupOrForward().add(attributes);

        Key key = factory.createKey();
        attributes.getKey().add(key);
        key.setFifths(new BigInteger("0"));

        Clef clef = factory.createClef();
        attributes.getClef().add(clef);
        clef.setSign(ClefSign.G);
        clef.setLine(new BigInteger("2"));

        return measure;
    }

    private static boolean addToXML(String line, ScorePartwise.Part.Measure measure) {
        if (line == null) {
            return false;
        }
        if (line.contains("time")) {
            parseTimeSignature(line);
            logger.info("Time Signature set.");
        } else if (line.contains("bpm")) {
            logger.info("BPM set.");
        } else if (line.contains("Octave")) {
            octave = Integer.parseInt(getInfoAfterColon(line));
            logger.info("Octave set.");
        } else if (line.contains("Clock")) { //do nothing for now
        } else {
            String noteLength = getInfoBeforeColon(line);
            String noteLetter = getInfoAfterColon(line);
            logger.info("length: " + noteLength + ", Note: " + noteLetter);

            Note note = factory.createNote();
            measure.getNoteOrBackupOrForward().add(note);

            if (noteLetter.equals("rest")) {
                note.setRest(factory.createRest());
            } else {
                Step step = org.audiveris.proxymusic.Step.valueOf((noteLetter));
                logger.info("Step: " + step);
                Pitch pitch = factory.createPitch();
                pitch.setStep(step);
                pitch.setOctave(octave);
                note.setPitch(pitch);
            }

            int duration = getDurationFromNoteLength(noteLength);
            note.setDuration(new BigDecimal(duration));
            totalDuration += duration;
           /* if(currentBeatNum > 20) {
                currentMeasureNum++;
                measure.setNumber(String.valueOf(currentMeasureNum));
                measure.getNoteOrBackupOrForward().add(attributes);
                Clef clef = factory.createClef();
                attributes.getClef().add(clef);
                clef.setSign(ClefSign.G);
                clef.setLine(new BigInteger("2"));
                currentBeatNum=1;
            } else currentBeatNum++;*/
            NoteType type = factory.createNoteType();
            note.setType(type);
            type.setValue(noteLength);
        }
        return true;
    }

    private static void parseTimeSignature(String line) {
        // line appears in file as, ex: "time:3/4"
//        String topNum = line.substring(5, 6);
//        timeBeats = Integer.parseInt(topNum);
//        String bottomNum = line.substring(7);
        Time time = factory.createTime();
        time.getTimeSignature().add(factory.createTimeBeats("4")); //automatically assigning for now
        time.getTimeSignature().add(factory.createTimeBeatType("4"));
        attributes.getTime().add(time);
    }


    public static String getInfoAfterColon(String arg) {
        return arg.substring(arg.lastIndexOf(":") + 1);
    }

    public static String getInfoBeforeColon(String arg) {
        int index = arg.indexOf(":");
        String subString = "";
        if (index != -1)
            subString = arg.substring(0, index);
        return subString;
    }

    private static int getDurationFromNoteLength(String noteLength) {
        return switch (noteLength) {
            case "16th" -> 1;
            case "eighth" -> 2;
            case "quarter" -> 4;
            case "half" -> 8;
            case "whole" -> 16;
            default -> 0;
        };
    }

    public static void marshalScore(ScorePartwise score) {
        logger.info("Calling tryMarshal...");
        long start = System.currentTimeMillis();

        //  Finally, marshal the proxy
        File xmlFile = new File("output/testFile" + System.currentTimeMillis() + ".musicxml");

        try (OutputStream os = new FileOutputStream(xmlFile)) {
            Marshalling.marshal(score, os, true, 2);
        } catch (IOException | Marshalling.MarshallingException e) {
            logger.severe(e.toString());
            throw new RuntimeException(e);
        }
        logger.info("Marshalling done in %d ms".formatted(System.currentTimeMillis() - start));
        logger.info("Score exported to " + xmlFile);
    }
}