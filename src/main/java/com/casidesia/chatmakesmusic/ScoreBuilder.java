package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.ParsedNoteOrRest;
import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;

public class ScoreBuilder {
    private static final Logger log = LoggerFactory.getLogger(ScoreBuilder.class);
    private static final ObjectFactory factory = new ObjectFactory();

    private static class Constants {
        private static final String SONG_TITLE = "Chats Song";
        private static final String COMPOSER = "The Composer";
    }

    // Global values, initialized in constructor
    private final ScorePartwise score;
    private final ScorePartwise.Part part;
    private final Attributes attributes;

    // Measure tracking
    private ScorePartwise.Part.Measure currentMeasure;
    private int currentMeasureNum;
    private int currentMeasureDivisions;
    private int totalDivisionsPerMeasure;

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

        currentMeasure = createMeasure();
    }

    private ScorePartwise.Part.Measure createMeasure() {
        ScorePartwise.Part.Measure measure = factory.createScorePartwisePartMeasure();
        measure.setNumber(String.valueOf(++currentMeasureNum));
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
        log.info("attributes Time Signature set to: {}/{}", attributes.getTime().get(0).getTimeSignature().get(0).getValue(), attributes.getTime().get(0).getTimeSignature().get(1).getValue());

        totalDivisionsPerMeasure = timeSignatureUpper * timeSignatureLower * NoteLength.QUARTER.getDuration();
        log.info("Current total divisions per measure: {}", totalDivisionsPerMeasure);

        attributes.setDivisions(BigDecimal.valueOf(totalDivisionsPerMeasure));
        log.info("Division attribute set to: {}", attributes.getDivisions());
    }

    public void addNote(ParsedNoteOrRest parsedNote) {
        log.info("Current parsed note: {}", parsedNote);

        currentMeasure.getNoteOrBackupOrForward().add(parsedNote.toXmlNote());

        currentMeasureDivisions += parsedNote.getDuration();
        log.info("Current Measure Divisions after addition: {}", currentMeasureDivisions);
        // TODO: Check measure boundaries and create new measures when necessary
    }

    public ScorePartwise getScore() {
        // TODO: Remove the following line once measures are implemented
        attributes.setDivisions(BigDecimal.valueOf(currentMeasureDivisions));

        return score;
    }
}
