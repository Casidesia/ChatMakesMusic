package com.casidesia.chatmakesmusic.enums;

import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.NoteType;

import java.math.BigDecimal;
import java.util.*;

public enum NoteLength {
    WHOLE("whole", 16),
    HALF("half", 8),
    QUARTER("quarter", 4),
    EIGHTH("eighth", 2),
    SIXTEENTH("16th", 1);

    private final String text;
    private final int duration;
    private static final List<NoteLength> VALUES = List.of(values());
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    NoteLength(String parsedName, int duration) {
        this.text = parsedName;
        this.duration = duration;
    }

    public static NoteLength tryParse(String name) {
        return VALUES.stream()
            .filter(length -> length.text.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public int getDuration() {
        return duration;
    }

    public String getText() {
        return text;
    }

    public void addDurationAndTypeToNote(Note note) {
        note.setDuration(BigDecimal.valueOf(duration));
        NoteType type = new NoteType();
        type.setValue(text);
        note.setType(type);
    }

    // Return the NoteLength with duration closest to (but not exceeding) the parameter duration
    public static NoteLength findNextShortestNoteLength(int duration) {
        return VALUES.stream()
            .sorted(Comparator.comparingInt(NoteLength::getDuration).reversed())
            .filter(nl -> nl.getDuration() <= duration)
            .findFirst()
            .orElse(null);
    }

    @Override
    public String toString() {
        return "NoteLength{" +
                "parsedName='" + text + '\'' +
                ", duration=" + duration +
                '}';
    }

    public static String randomLength()  {
        NoteLength length = VALUES.get(RANDOM.nextInt(SIZE));
        return length.getText();
    }
}
