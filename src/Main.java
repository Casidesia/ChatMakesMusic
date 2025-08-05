import com.sun.jdi.request.StepRequest;
import org.audiveris.proxymusic.*;
import org.audiveris.proxymusic.util.Marshalling;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Main {
    private final static Logger logger = Logger.getLogger(Main.class.getName());
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Main.class);
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
    static ObjectFactory factory = new ObjectFactory();
    // Allocate the score partwise
    static ScorePartwise scorePartwise = factory.createScorePartwise();
    // Identification
    static Identification identification = factory.createIdentification();
    // PartList
    static PartList partList = factory.createPartList();
    static PartName partName = factory.createPartName();
    // ScorePart in scorePartwise
    static ScorePartwise.Part part = factory.createScorePartwisePart();
    // Measure
    static ScorePartwise.Part.Measure measure = factory.createScorePartwisePartMeasure();
    // Attributes
    static Attributes attributes = factory.createAttributes();
    // Key
    static Key key = factory.createKey();
    // Time
    static Time time = factory.createTime();
    // Clef
    static Clef clef = factory.createClef();
    // Note
    static Note note = factory.createNote();
    // Pitch
    static Pitch pitch = factory.createPitch();
    // Type
    static NoteType type = factory.createNoteType();
    static Metronome metronome = factory.createMetronome();
    static Direction
    direction = factory.createDirection();
    static DirectionType directionType = factory.createDirectionType();
    // Scorepart in partList
    static ScorePart scorePart = factory.createScorePart();
    static ScorePartwise.Part.Measure meas = factory.createScorePartwisePartMeasure();
    static int countNum = 0;
    static String topNum;
    static String bottomNum;
    static Rest rest = new Rest();
    static int measureNumber = 1;

    public static void main(String[] args) throws FileNotFoundException {


        FileHandler fh;
        try {


            fh = new FileHandler("ChatMakesMusic.log");
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);

            logger.info("Application successfully started.");

            String path = "D:\\Creative\\Vtuber\\Assets\\Stream\\streammusic.txt";
            InputStream is = new FileInputStream(path);
            try (Scanner sc = new Scanner(is, StandardCharsets.UTF_8)) {
                initXMLFile();
                while (sc.hasNextLine()) {

                        AddToXML(sc.nextLine());
                    }

            } catch (SecurityException e) {
                e.printStackTrace();
            }
            tryMarshal();

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void initXMLFile() {
        // Movement
        scorePartwise.setMovementTitle("Chats Song");

        scorePartwise.setIdentification(identification);

        TypedText typedText = factory.createTypedText();
        typedText.setValue("The Composer");
        typedText.setType("composer");
        identification.getCreator().add(typedText);
        scorePartwise.setPartList(partList);

        partList.getPartGroupOrScorePart().add(scorePart);
        scorePart.setId("P1");

        scorePart.setPartName(partName);
        partName.setValue("Music");
        scorePartwise.getPart().add(part);
        part.setId(scorePart);

       // part.getMeasure().add(measure);
        //measure.setNumber(String.valueOf(measureNumber));
        measure.getNoteOrBackupOrForward().add(attributes);
        // Divisions
        attributes.setDivisions(new BigDecimal(1));
        attributes.getKey().add(key);
        key.setFifths(new BigInteger("0"));
        attributes.getTime().add(time);
        attributes.getClef().add(clef);
        //  clef.setSign(ClefSign.G);
        // clef.setLine(new BigInteger("2"));

        measure.getNoteOrBackupOrForward().add(note);
        note.setPitch(pitch);
        pitch.setStep(Step.C);

        // Duration
        //note.setDuration(new BigDecimal(4));

        //type.setValue("whole");
        //note.setType(type);

        //direction.getDirectionType().add(directionType);

    }

    private static void AddToXML(String arg) throws ClassNotFoundException, InstantiationException, IllegalAccessException {


        if (arg.contains("time")) {
            //line appears in file as, ex: "time:3/4"
            topNum = arg.substring(5, 6);
            bottomNum = arg.substring(7);
            time.getTimeSignature().add(factory.createTimeBeats(topNum));
            time.getTimeSignature().add(factory.createTimeBeatType(bottomNum));
            logger.info("Time Signature set.");
        }
//        else if(arg.contains("bpm")){
//            String bpm = getInfoAfterColon(arg);
//            metronome.setBeatUnit(directionData.beatUnit);
//            for (int i = 0; i < directionData.dots; i++) {
//                metronome.getBeatUnitDot().add(factory.createEmpty());
//            }
//
//            // Doesn't seem to be recognized by Finale or MuseScore
//            //        BeatUnitTied but = factory.createBeatUnitTied();
//            //        but.setBeatUnit("eighth");
//            //        metronome.getBeatUnitTied().add(but);
//
//            PerMinute perMinute = factory.createPerMinute();
//            perMinute.setValue(directionData.perMinute);
//            metronome.setPerMinute(perMinute);
//            metronome.setParentheses(directionData.parentheses);
//            directionType.setMetronome(metronome);
//           // logger.info("BPM set.");
//        }
        else if (arg.contains("Octave")) {
            String octave = getInfoAfterColon(arg);
            pitch.setOctave(Integer.parseInt(octave));
            logger.info("Octave set.");
        } else if (arg.contains("Clock")) { //do nothing for now
        } else {

            measure.getNoteOrBackupOrForward().add(note);
            String noteLenth = getInfoBeforeColon(arg);
            String noteLetter = getInfoAfterColon(arg);
            logger.info("lenth: " + noteLenth + ", Note: " + noteLetter);

            if (noteLetter.equals("rest")) {
                rest.setMeasure(YesNo.YES);
                //type.setValue();
            } else {
                Class<?> Step = Class.forName("org.audiveris.proxymusic.Step");
                Step step = org.audiveris.proxymusic.Step.valueOf((noteLetter));
                pitch.setStep(step);
            }
           // type.setValue(noteLenth);
            type = factory.createNoteType();
            note.setType(type);
            note.setDuration(new BigDecimal(1));
            measure.getNoteOrBackupOrForward().add(note);
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

    public static void tryMarshal()
            throws Exception {
        logger.info("Calling tryMarshal...");
        //  Finally, marshal the proxy

        File xmlFile = new File("testFile" + System.currentTimeMillis() + ".musicxml");
        OutputStream os = new FileOutputStream(xmlFile);
        long start = System.currentTimeMillis();

        Marshalling.marshal(scorePartwise, os, true, 2);

        logger.info("Marshalling done in {} ms");
        logger.info("Score exported to {}" + xmlFile);
        os.close();
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

