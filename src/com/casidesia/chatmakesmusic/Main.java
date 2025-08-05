package com.casidesia.chatmakesmusic;

import org.audiveris.proxymusic.*;
import org.audiveris.proxymusic.util.Marshalling;

import java.io.*;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    private static final ObjectFactory objectFactory = new ObjectFactory();

    private static final NoteData[] noteData = new NoteData[]
            {
                    // 0
                    new NoteData(Step.C, 4, new BigDecimal(4), "whole"),
                    // 1
                    new NoteData(Step.E, 4, new BigDecimal(4), "whole"),
                    // 2
                    new NoteData(Step.G, 4, new BigDecimal(4), "whole")};
    private static final DirectionData directionData =
            new DirectionData("quarter", 1, "c. 100-120", YesNo.YES, new BigDecimal(110));

    static ScorePartwise.Part.Measure meas = objectFactory.createScorePartwisePartMeasure();
    static int countNum = 0;
    static String topNum;
    static String bottomNum;
    static Rest rest = new Rest();
    static int measureNumber = 1;

    public static void main(String[] args) throws IOException {
        initLogger();
        logger.info("Application successfully started.");

        ScorePartwise score = initXMLFile();

        String path = "D:\\Creative\\Vtuber\\Assets\\Stream\\streammusic.txt";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            while(addToXML(reader.readLine(), score));
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        marshalScore(score);
    }

    private static void initLogger() throws IOException {
        FileHandler fh = new FileHandler("ChatMakesMusic.log");
        fh.setFormatter(new SimpleFormatter());
        logger.addHandler(fh);
    }

    public static ScorePartwise initXMLFile() {
        ScorePartwise score = objectFactory.createScorePartwise();

        // Movement
        score.setMovementTitle("Chats Song");

        Identification identification = objectFactory.createIdentification();
        score.setIdentification(identification);
        TypedText typedText = objectFactory.createTypedText();
        typedText.setValue("The Composer");
        typedText.setType("composer");
        identification.getCreator().add(typedText);

        PartList partList = objectFactory.createPartList();
        score.setPartList(partList);

        ScorePart scorePart = objectFactory.createScorePart();
        scorePart.setId("P1");
        partList.getPartGroupOrScorePart().add(scorePart);
        PartName partName = objectFactory.createPartName();
        partName.setValue("Music");
        scorePart.setPartName(partName);

        ScorePartwise.Part part = objectFactory.createScorePartwisePart();
        part.setId(scorePart);
        score.getPart().add(part);


        // Example from HelloWorld

        ScorePartwise.Part.Measure measure = objectFactory.createScorePartwisePartMeasure();
       // part.getMeasure().add(measure);
        //measure.setNumber(String.valueOf(measureNumber));
        Attributes attributes = objectFactory.createAttributes();
        measure.getNoteOrBackupOrForward().add(attributes);
        // Divisions
        attributes.setDivisions(new BigDecimal(1));

        Key key = objectFactory.createKey();
        attributes.getKey().add(key);
        key.setFifths(new BigInteger("0"));
        Time time = objectFactory.createTime();
        attributes.getTime().add(time);

        Clef clef = objectFactory.createClef();
        attributes.getClef().add(clef);
        //  clef.setSign(ClefSign.G);
        // clef.setLine(new BigInteger("2"));

        Note note = objectFactory.createNote();
        measure.getNoteOrBackupOrForward().add(note);
        Pitch pitch = objectFactory.createPitch();
        note.setPitch(pitch);
        pitch.setStep(Step.C);

        // Duration
        //note.setDuration(new BigDecimal(4));

        //type.setValue("whole");
        //note.setType(type);

//        Direction direction = objectFactory.createDirection();
//        DirectionType directionType = objectFactory.createDirectionType();
//        direction.getDirectionType().add(directionType);

        return score;
    }

    private static boolean addToXML(String line, ScorePartwise score) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        if (line == null)
            return false;

        if (line.contains("time")) {
            //line appears in file as, ex: "time:3/4"
            topNum = line.substring(5, 6);
            bottomNum = line.substring(7);
            Time time = objectFactory.createTime();
            time.getTimeSignature().add(objectFactory.createTimeBeats(topNum));
            time.getTimeSignature().add(objectFactory.createTimeBeatType(bottomNum));

            logger.info("Time Signature set.");
        }
//        else if(line.contains("bpm")){
//            String bpm = getInfoAfterColon(line);
//            Metronome metronome = objectFactory.createMetronome();
//            metronome.setBeatUnit(directionData.beatUnit);
//            for (int i = 0; i < directionData.dots; i++) {
//                metronome.getBeatUnitDot().add(objectFactory.createEmpty());
//            }
//
//            // Doesn't seem to be recognized by Finale or MuseScore
//            //        BeatUnitTied but = objectFactory.createBeatUnitTied();
//            //        but.setBeatUnit("eighth");
//            //        metronome.getBeatUnitTied().add(but);
//
//            PerMinute perMinute = objectFactory.createPerMinute();
//            perMinute.setValue(directionData.perMinute);
//            metronome.setPerMinute(perMinute);
//            metronome.setParentheses(directionData.parentheses);
//            DirectionType directionType = objectFactory.createDirectionType();
//            directionType.setMetronome(metronome);
//           // logger.info("BPM set.");
//        }
        else if (line.contains("Octave")) {
            String octave = getInfoAfterColon(line);
            Pitch pitch = objectFactory.createPitch();
            pitch.setOctave(Integer.parseInt(octave));
            logger.info("Octave set.");
        } else if (line.contains("Clock")) { //do nothing for now
        } else {
            ScorePartwise.Part.Measure measure = objectFactory.createScorePartwisePartMeasure();
            Note note = objectFactory.createNote();
            measure.getNoteOrBackupOrForward().add(note);
            String noteLenth = getInfoBeforeColon(line);
            String noteLetter = getInfoAfterColon(line);
            logger.info("lenth: " + noteLenth + ", Note: " + noteLetter);

            if (noteLetter.equals("rest")) {
                rest.setMeasure(YesNo.YES);
                //type.setValue();
            } else {
                Pitch pitch = objectFactory.createPitch();
                Step step = org.audiveris.proxymusic.Step.valueOf((noteLetter));
                pitch.setStep(step);
            }
           // type.setValue(noteLenth);
            NoteType type = objectFactory.createNoteType();
            note.setType(type);
            note.setDuration(new BigDecimal(1));
            measure.getNoteOrBackupOrForward().add(note);

            ScorePartwise.Part part = score.getPart().get(0);
            if (countNum<Integer.valueOf(topNum))
                countNum++;
            else {
                meas.getNoteOrBackupOrForward().add(true);
                countNum=0;
                measureNumber++;
                part.getMeasure().add(measure);
                measure.setNumber(String.valueOf(measureNumber));
            }
            part.getMeasure().add(meas);
            //meas.setNumber("" + 1);
        }
        return true;
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

    public static void marshalScore(ScorePartwise score) {
        logger.info("Calling tryMarshal...");
        long start = System.currentTimeMillis();

        //  Finally, marshal the proxy
        File xmlFile = new File("testFile" + System.currentTimeMillis() + ".musicxml");

        try (OutputStream os = new FileOutputStream(xmlFile)) {
            Marshalling.marshal(score, os, true, 2);
        } catch (IOException | Marshalling.MarshallingException e) {
            logger.severe(e.toString());
            throw new RuntimeException(e);
        }
        logger.info("Marshalling done in %d ms".formatted(System.currentTimeMillis() - start));
        logger.info("Score exported to " + xmlFile);
    }

    //~ Inner Classes ------------------------------------------------------------------------------
    private static class AttrData {
        //~ Instance fields ------------------------------------------------------------------------

        final BigDecimal divisions;

        final BigInteger fifths;

        final String beats;

        final String beatType;

        final ClefSign clefSign;

        final BigInteger clefLine;

        //~ Constructors ---------------------------------------------------------------------------
        public AttrData(BigDecimal divisions,
                        BigInteger fifths,
                        String beats,
                        String beatType,
                        ClefSign clefSign,
                        BigInteger clefLine) {
            this.divisions = divisions;
            this.fifths = fifths;
            this.beats = beats;
            this.beatType = beatType;
            this.clefSign = clefSign;
            this.clefLine = clefLine;
        }
    }

    private static class DirectionData {
        final String beatUnit;

        final int dots;

        final String perMinute;

        final YesNo parentheses;

        final BigDecimal tempo;

        public DirectionData(String beatUnit,
                             int dots,
                             String perMinute,
                             YesNo parentheses,
                             BigDecimal tempo) {
            this.beatUnit = beatUnit;
            this.dots = dots;
            this.perMinute = perMinute;
            this.parentheses = parentheses;
            this.tempo = tempo;
        }
    }

    private static class MeasData {
        //~ Instance fields ------------------------------------------------------------------------

        final String number;

        final List<Object> objects;

        //~ Constructors ---------------------------------------------------------------------------
        public MeasData(String number,
                        List<Object> objects) {
            this.number = number;
            this.objects = objects;
        }
    }

    private static class NoteData {
        //~ Instance fields ------------------------------------------------------------------------

        final Step pitchStep;

        final int pitchOctave;

        final BigDecimal duration;

        final String type;

        //~ Constructors ---------------------------------------------------------------------------
        public NoteData(Step pitchStep,
                        int pitchOctave,
                        BigDecimal duration,
                        String type) {
            this.pitchStep = pitchStep;
            this.pitchOctave = pitchOctave;
            this.duration = duration;
            this.type = type;
        }
    }

    private static class PartData {
        //~ Instance fields ------------------------------------------------------------------------

        final String id;

        final String name;

        final List<MeasData> measures;

        //~ Constructors ---------------------------------------------------------------------------
        public PartData(String id,
                        String name,
                        List<MeasData> measures) {
            this.id = id;
            this.name = name;
            this.measures = measures;
        }
    }
}

