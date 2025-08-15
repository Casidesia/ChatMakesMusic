package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.AttributesHolder;
import com.casidesia.chatmakesmusic.data.ParsedNoteOrRest;
import org.audiveris.proxymusic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;

public class ScoreBuilder {
    private static final Logger log = LoggerFactory.getLogger(ScoreBuilder.class);
    private static final ObjectFactory factory = new ObjectFactory();



    private static class Constants {
        private static final LocalDate d = LocalDate.now();

        private static final String SONG_TITLE = "Chat's Song (" + d + ")";
        private static final String COMPOSER = "Casidesia's Chat (" + d + ")";
    }

    // TODO: DELETE THIS
    private static class CurrentAttributes {
        private ScorePartwise.Part.Measure currentMeasure;
        private int currentMeasureNum;
        private int currentMeasureDivisions;
        private int maxDivisionsPerMeasure;
    }

    // Global values, initialized in constructor
    private final ScorePartwise score;
    private final ScorePartwise.Part part;
    private final CurrentAttributes currentAttributes = new CurrentAttributes();
    private final AttributesHolder attributesBuilder = new AttributesHolder();
    private final Attributes attributes;

    public ScoreBuilder() {
        this(Constants.SONG_TITLE, Constants.COMPOSER);
    }

    public ScoreBuilder(String title, String composer) {
        score = factory.createScorePartwise();
        score.setMovementTitle(title);
        TypedText typedText = factory.createTypedText();
        typedText.setValue(composer);
        typedText.setType("composer");
        Identification identification = factory.createIdentification();
        identification.getCreator().add(typedText);
        score.setIdentification(identification);
        score.setPartList(factory.createPartList());

        ScorePart scorePart = factory.createScorePart();
        ScoreInstrument scoreInstrument = new ScoreInstrument();
        scoreInstrument.setInstrumentName("Piano");
        scorePart.getScoreInstrument().add(scoreInstrument);
        scorePart.setId("P1");
        PartName partName = factory.createPartName();
        partName.setValue("Music");
        scorePart.setPartName(partName);
        score.getPartList().getPartGroupOrScorePart().add(scorePart);

        part = factory.createScorePartwisePart();
        part.setId(scorePart);
        score.getPart().add(part);

        attributes = factory.createAttributes();
        Key key = factory.createKey();
        key.setFifths(BigInteger.ZERO);
        log.info("Current fifths for key: {}", key.getFifths().toString());

        Clef clef = factory.createClef();
        clef.setSign(ClefSign.G);
        log.info("Current set clef sign: {}", clef.getSign().toString());

        attributes.getKey().add(key);
        attributes.getClef().add(clef);

        log.info("Attribute for key set to: {} fifths.", attributes.getKey().getFirst().getFifths().toString());
        log.info("Attribute for Clef set to: {}", attributes.getClef().getFirst().getSign().toString());

        currentAttributes.currentMeasure = createMeasure();
    }

    private ScorePartwise.Part.Measure createMeasure() {
        ScorePartwise.Part.Measure measure = factory.createScorePartwisePartMeasure();
        measure.setNumber(String.valueOf(++currentAttributes.currentMeasureNum));
        measure.getNoteOrBackupOrForward().add(attributes);
        part.getMeasure().add(measure);
        return measure;
    }

    public void setTimeSignature(int timeSignatureUpper, int timeSignatureLower) {
        Time timeSignature = factory.createTime();

        timeSignature.getTimeSignature().add(factory.createTimeBeats(String.valueOf(timeSignatureUpper)));
        timeSignature.getTimeSignature().add(factory.createTimeBeatType(String.valueOf(timeSignatureLower)));
        log.info("Time Signature set to: {}/{}", timeSignature.getTimeSignature().get(0).getValue(), timeSignature.getTimeSignature().get(1).getValue());

        attributes.getTime().add(timeSignature);
        log.info("attributes Time Signature set to: {}/{}", attributes.getTime().getLast().getTimeSignature().get(0).getValue(), attributes.getTime().getLast().getTimeSignature().get(1).getValue());
        //currentAttributes.currentMeasure.getNoteOrBackupOrForward().add
        currentAttributes.maxDivisionsPerMeasure = timeSignatureUpper * timeSignatureLower;
        log.info("Current total divisions per measure: {}", currentAttributes.maxDivisionsPerMeasure);

        attributes.setDivisions(BigDecimal.valueOf(currentAttributes.maxDivisionsPerMeasure));
        log.info("Division attribute set to: {}", attributes.getDivisions());
    }
    public void setInstrument(String instrumentString) {
        ScoreInstrument scoreInstrument = factory.createScoreInstrument();
        scoreInstrument.setInstrumentName(instrumentString);
        ScorePart scorePart = (ScorePart) this.score.getPartList().getPartGroupOrScorePart().getFirst();
        scorePart.getScoreInstrument().set(0,scoreInstrument);
        score.getPartList().getPartGroupOrScorePart().set(0,scorePart);
    }

    public void addNote(ParsedNoteOrRest parsedNote) {
        log.info("Current parsed note: {}", parsedNote);


        if(currentAttributes.maxDivisionsPerMeasure < currentAttributes.currentMeasureDivisions + parsedNote.getDuration()) {
            int finishCurrentMeasure = currentAttributes.maxDivisionsPerMeasure - currentAttributes.currentMeasureDivisions;
            int firstNoteOfNewMeasure = parsedNote.getDuration() - finishCurrentMeasure ;

            Note finishMeasureNote = parsedNote.toXmlNote();
            finishMeasureNote.setDuration(BigDecimal.valueOf(finishCurrentMeasure));
            currentAttributes.currentMeasure.getNoteOrBackupOrForward().add(finishMeasureNote);

            currentAttributes.currentMeasure = createMeasure();


            Note newMeasureNote = parsedNote.toXmlNote();
            newMeasureNote.setDuration(BigDecimal.valueOf(finishCurrentMeasure));
            currentAttributes.currentMeasure.getNoteOrBackupOrForward().add(newMeasureNote);

            currentAttributes.currentMeasureDivisions = firstNoteOfNewMeasure;

        }else {
            currentAttributes.currentMeasure.getNoteOrBackupOrForward().add(parsedNote.toXmlNote());
            currentAttributes.currentMeasureDivisions += parsedNote.getDuration();
            log.info("Current Measure Divisions after addition: {}", currentAttributes.currentMeasureDivisions);
        }




    }

    public ScorePartwise getScore() {
        return score;
    }
}
