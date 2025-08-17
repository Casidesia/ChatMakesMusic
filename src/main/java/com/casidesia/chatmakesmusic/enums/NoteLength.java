package com.casidesia.chatmakesmusic.enums;

import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.NoteType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum NoteLength {
    WHOLE("whole", 16),
    HALF("half", 8),
    QUARTER("quarter", 4),
    EIGHTH("eighth", 2),
    SIXTEENTH("16th", 1);

    private final String text;
    private final int duration;
    private static final List<NoteLength> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    NoteLength(String parsedName, int duration) {
        this.text = parsedName;
        this.duration = duration;
    }

    public static NoteLength tryParse(String name) {
        return Arrays.stream(NoteLength.values())
            .filter(length -> length.text.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public int getDuration() {
        return duration;
    }

    public void addDurationAndTypeToNote(Note note) {
        note.setDuration(BigDecimal.valueOf(duration));
        NoteType type = new NoteType();
        type.setValue(text);
        note.setType(type);
    }

    @Override
    public String toString() {
        return "NoteLength{" +
                "parsedName='" + text + '\'' +
                ", duration=" + duration +
                '}';
    }
    String getText(){
        return this.text;
    }

    public static String randomLength()  {
        NoteLength length = VALUES.get(RANDOM.nextInt(SIZE));
        return length.getText();
    }
}
