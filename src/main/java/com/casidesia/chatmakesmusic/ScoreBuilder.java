package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.ParsedNoteOrRest;
import com.casidesia.chatmakesmusic.enums.NoteLength;
import com.casidesia.chatmakesmusic.util.LogFactory;
import org.audiveris.proxymusic.*;

import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.logging.Logger;

public class ScoreBuilder {
    private static final ObjectFactory factory = new ObjectFactory();
    private static final Logger log = LogFactory.getLoggerForClass(ScoreBuilder.class);

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
        Clef clef = factory.createClef();
        clef.setSign(ClefSign.G);
        clef.setLine(BigInteger.TWO);
        attributes.getKey().add(key);

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
        attributes.getTime().add(timeSignature);

        totalDivisionsPerMeasure = timeSignatureUpper * timeSignatureLower * NoteLength.QUARTER.getDuration();
        attributes.setDivisions(BigDecimal.valueOf(totalDivisionsPerMeasure));
    }

    public void addNote(ParsedNoteOrRest parsedNote) {
        currentMeasure.getNoteOrBackupOrForward().add(parsedNote.toXmlNote());

        currentMeasureDivisions += parsedNote.getDuration();
        // TODO: Check measure boundaries and create new measures when necessary
    }

    public ScorePartwise getScore() {
        // TODO: Remove the following line once measures are implemented
        attributes.setDivisions(BigDecimal.valueOf(currentMeasureDivisions));

        return score;
    }
}
