package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.AttributesHolder;
import com.casidesia.chatmakesmusic.data.ParsedNoteOrRest;
import com.casidesia.chatmakesmusic.enums.Instrument;
import com.casidesia.chatmakesmusic.enums.NoteLength;
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
    // Global values, initialized in constructor
    private final Attributes initialAttributes;
    private final ScorePartwise score;
    private final ScorePartwise.Part part;
    private final AttributesHolder attributesHolder = new AttributesHolder();
    // Measure tracking
    private ScorePartwise.Part.Measure currentMeasure;
    private int currentMeasureNum;
    private int currentMeasureDivisions;
    private int maxDivisionsPerMeasure;

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

        attributesHolder.setKeyFifths(0);
        currentMeasure = createMeasure();
        initialAttributes = (Attributes) currentMeasure.getNoteOrBackupOrForward().getFirst();
        setInstrument(Instrument.PIANO);
    }

    private ScorePartwise.Part.Measure createMeasure() {
        ScorePartwise.Part.Measure measure = factory.createScorePartwisePartMeasure();
        measure.setNumber(String.valueOf(++currentMeasureNum));
        measure.getNoteOrBackupOrForward().add(attributesHolder.getCurrentAttributes());
        part.getMeasure().add(measure);
        maxDivisionsPerMeasure = attributesHolder.getDivisions();
        return measure;
    }

    public void setTimeSignature(int timeSignatureUpper, int timeSignatureLower) {
        if (currentMeasureNum == 1) {
            Time timeSignature = factory.createTime();
            timeSignature.getTimeSignature().add(factory.createTimeBeats(String.valueOf(timeSignatureUpper)));
            timeSignature.getTimeSignature().add(factory.createTimeBeatType(String.valueOf(timeSignatureLower)));
            initialAttributes.getTime().add(timeSignature);
            attributesHolder.setInitialTime(timeSignatureUpper, timeSignatureLower);
            initialAttributes.setDivisions(BigDecimal.valueOf(attributesHolder.getDivisions()));
        } else
            attributesHolder.setTime(timeSignatureUpper, timeSignatureLower);
    }

    public void setInstrument(Instrument instrument) {
        ScoreInstrument scoreInstrument = factory.createScoreInstrument();
        scoreInstrument.setInstrumentName(instrument.getInstrumentName());
        scoreInstrument.setInstrumentSound(instrument.getInstrumentSound());

        Clef newClef = new Clef();
        newClef.setSign(instrument.getSign());
        newClef.setLine(instrument.getClefLine());
        if (Instrument.needsClefOctaveChange(instrument))
            newClef.setClefOctaveChange(BigInteger.valueOf(-1));

        initialAttributes.getClef().clear();
        initialAttributes.getClef().add(newClef);

        Transpose transpose = new Transpose();
        transpose.setChromatic(instrument.getChromatic());
        transpose.setDiatonic(instrument.getDiatonic());
        transpose.setOctaveChange(instrument.getOctaveChange());

        initialAttributes.getTranspose().add(transpose);

        ScorePart scorePart = (ScorePart) this.score.getPartList().getPartGroupOrScorePart().getFirst();
        scorePart.getPartName().setValue(instrument.getInstrumentName());
        scorePart.getScoreInstrument().clear();
        scorePart.getScoreInstrument().add(scoreInstrument);
    }

    public void addNote(ParsedNoteOrRest parsedNote) {
        log.info("Current parsed note: {}", parsedNote);

        if (maxDivisionsPerMeasure < currentMeasureDivisions + parsedNote.getDuration()) {
            int finishCurrentMeasure = maxDivisionsPerMeasure - currentMeasureDivisions;
            int firstNoteOfNewMeasure = parsedNote.getDuration() - finishCurrentMeasure;

            Note finishMeasureNote = parsedNote.toXmlNote();
            finishMeasureNote.setDuration(BigDecimal.valueOf(finishCurrentMeasure));
            currentMeasure.getNoteOrBackupOrForward().add(finishMeasureNote);

            currentMeasure = createMeasure();

            Note newMeasureNote = parsedNote.toXmlNote();
            newMeasureNote.setDuration(BigDecimal.valueOf(finishCurrentMeasure));
            currentMeasure.getNoteOrBackupOrForward().add(newMeasureNote);

            currentMeasureDivisions = firstNoteOfNewMeasure;

        } else {
            currentMeasure.getNoteOrBackupOrForward().add(parsedNote.toXmlNote());
            currentMeasureDivisions += parsedNote.getDuration();
            log.info("Current Measure Divisions after addition: {}", currentMeasureDivisions);
        }
    }

    public ScorePartwise getScore() {
        return score;
    }

    private static class Constants {
        private static final LocalDate d = LocalDate.now();

        private static final String SONG_TITLE = "Chat's Song (" + d + ")";
        private static final String COMPOSER = "Casidesia's Chat (" + d + ")";
    }
}
