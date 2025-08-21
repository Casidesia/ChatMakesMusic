package com.casidesia.chatmakesmusic.enums;

import org.audiveris.proxymusic.ClefSign;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Instrument {
    FIVE_STR_ELEC_BASS("5-str. Electric Bass", "pluck.bass.electric", 0, ClefSign.F, 4, 0, 0, -1),
    ACCORDION("Accordion", "keyboard.accordion", 0, ClefSign.G, 2, 0, 0, 0),
    ACOUSTIC_BASS("Acoustic Bass", "pluck.bass.acoustic", 0, ClefSign.F, 4, 0, 0, -1),
    ACOUSTIC_GUITAR("Acoustic Guitar", "pluck.guitar.acoustic", 0, ClefSign.G, 2, 0, 0, 0),//clef change of -1
    ALTO("Alto", "voice.alto", 0, ClefSign.G, 2, 0, 0, 0),
    ALTO_SAXOPHONE("Alto Saxophone", "wind.reed.saxophone.alto", 3, ClefSign.G, 2, -5, -9, 0),
    BANJO("Banjo", "pluck.banjo", 0, ClefSign.G, 2, 0, 0, 0), //clef octave change of -1
    BARITONE("Baritone", "voice.baritone", 0, ClefSign.F, 4, 0, 0, 0),
    BARITONE_SAXOPHONE("Baritone Saxophone", "wind.reed.saxophone.baritone", 3, ClefSign.G, 2, -5, -9, -1),
    BASS("Bass", "voice.bass", 0, ClefSign.F, 4, 0, 0, 0),
    BASS_CLARINET("Bass Clarinet", "wind.reed.clarinet.bass", 2, ClefSign.G, 2, -1, -2, -1),
    BASS_GUITAR("Bass Guitar", "pluck.bass", 0, ClefSign.F, 4, 0, 0, -1),
    BASSOON("Bassoon", "wind.reed.bassoon", 0, ClefSign.F, 4, 0, 0, 0),
    CLARINET("Clarinet", "wind.reed.clarinet.bflat", 2, ClefSign.G, 2, -1, -2, 0),
    CLASSICAL_GUITAR("Classical Guitar", "pluck.guitar.nylon-string", 0, ClefSign.G, 2, 0, 0, 0), //TODO: seperate those that need to be CLEFF octave changes. this is one of -1.
    CONTRABASS("Contrabass", "strings.contrabass", 0, ClefSign.F, 4, 0, 0, -1),
    CORNET("Cornet", "brass.cornet", 2, ClefSign.G, 2, -1, -2, 0),
    ELECTRIC_BASS("Electric Bass", "pluck.bass.electric", 0, ClefSign.F, 4, 0, 0, -1),
    ELECTRIC_GUITAR("Electric Guitar", "pluck.guitar.electric", 0, ClefSign.G, 2, 0, 0, 0),//clef octave change -1
    HARP("Harp", "pluck.harp", 0, ClefSign.G, 0, 0, 0, 0),
    HARPSICHORD("Harpsichord", "keyboard.harpsichord", 0, ClefSign.G, 2, 0, 0, 0),
    HORN("Horn", "brass.french-horn", 1, ClefSign.G, 2, -4, -7, 0),
    KAZOO("Kazoo", "voice.kazoo", 0, ClefSign.G, 2, 0, 0, 0),
    MEN("Men", "voice.male", 0, ClefSign.F, 4, 0, 0, 0),
    MEZZO_SOPRANO("Mezzo-soprano", "voice.mezzo-soprano", 0, ClefSign.G, 2, 0, 0, 0),
    OBOE("Oboe", "wind.reed.oboe", 0, ClefSign.G, 2, 0, 0, 0),
    ORGAN("Organ", "keyboard.organ", 0, ClefSign.G, 2, 0, 0, 0),
    PIANO("Piano", "keyboard.piano", 0, ClefSign.G, 2, 0, 0, 0),
    PICCOLO("Piccolo", "wind.flutes.flute.piccolo", 0, ClefSign.G, 2, 0, 0, 1),
    PIPE_ORGAN("Pipe Organ", "keyboard.organ.pipe", 0, ClefSign.G, 2, 0, 0, 0),
    SOPRANO("Soprano", "voice.soprano", 0, ClefSign.G, 2, 0, 0, 0),
    SOPRANO_SAXOPHONE("Soprano Saxophone", "wind.reed.saxophone.soprano", 2, ClefSign.G, 2, -1, -2, 0),
    TENOR("Tenor", "voice.tenor", 0, ClefSign.G, 2, 0, 0, 0),
    TENOR_SAXOPHONE("Tenor Saxophone", "wind.reed.saxophone.tenor", 2, ClefSign.G, 2, -1, -2, -1),
    TROMBONE("Trombone", "brass.trombone", 0, ClefSign.F, 4, 0, 0, 0),
    TRUMPET("Trumpet", "brass.trumpet.bflat", 2, ClefSign.G, 2, -1, -2, 0),
    TUBA("Tuba", "brass.tuba", 0, ClefSign.F, 4, 0, 0, 0),
    UKULELE("Ukulele", "pluck.ukulele", 0, ClefSign.G, 2, 0, 0, 0),
    VIOLA("Viola", "strings.viola", 0, ClefSign.C, 3, 0, 0, 0),
    VIOLIN("Violin", "strings.violin", 0, ClefSign.G, 2, 0, 0, 0),
    VIOLONCELLO("Violoncello", "strings.cello", 0, ClefSign.F, 4, 0, 0, 0),
    VOICE("Voice", "voice.vocals", 0, ClefSign.G, 2, 0, 0, 0),
    WOMEN("Women", "voice.female", 0, ClefSign.G, 2, 0, 0, 0);


    private static final List<Instrument> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();
    private final String instrumentName;
    private final String instrumentSound;
    private final int keyFifths;
    private final ClefSign sign;
    private final int clefLine;
    private final int diatonic;
    private final int chromatic;
    private final int OctaveChange;


    Instrument(String instrumentName, String instrumentSound, int keyFifths, ClefSign sign, int clefLine, int diatonic, int chromatic, int octaveChange) {
        this.instrumentName = instrumentName;
        this.instrumentSound = instrumentSound;
        this.keyFifths = keyFifths;
        this.sign = sign;
        this.clefLine = clefLine;
        this.chromatic = chromatic;
        this.diatonic = diatonic;
        this.OctaveChange = octaveChange;

    }

    public static boolean needsClefOctaveChange(Instrument instrument) {
        return (instrument == ACOUSTIC_GUITAR || instrument == BANJO
                || instrument == CLASSICAL_GUITAR || instrument == ELECTRIC_GUITAR);
    }

    public static Instrument tryParse(String name) {
        return Arrays.stream(Instrument.values())
                .filter(instrument -> instrument.instrumentName.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Instrument randomInstrument() {
        Instrument instrument = VALUES.get(RANDOM.nextInt(SIZE));
        return instrument;
    }

    public String getInstrumentSound() {
        return instrumentSound;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public int getKeyFifths() {
        return keyFifths;
    }

    public ClefSign getSign() {
        return sign;
    }

    public BigInteger getClefLine() {
        return BigInteger.valueOf(clefLine);
    }

    public BigInteger getDiatonic() {
        return BigInteger.valueOf(diatonic);
    }

    public BigDecimal getChromatic() {
        return BigDecimal.valueOf(chromatic);
    }

    public BigInteger getOctaveChange() {
        return BigInteger.valueOf(OctaveChange);
    }

    @Override
    public String toString() {
        return "Instrument{" +
                "parsedName='" + instrumentName + '\'' +
                ", instrumentSound='" + instrumentSound + '\'' +
                ", keyFifths=" + keyFifths +
                ", clefSign='" + sign + '\'' +
                ", clefLine=" + clefLine +
                ", diatonic=" + diatonic +
                ", chromatic=" + chromatic +
                ", OctaveChange=" + OctaveChange +
                '}';
    }
}
