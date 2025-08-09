package com.casidesia.chatmakesmusic.enums;

import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.NoteType;

import java.math.BigDecimal;
import java.util.Arrays;

public enum NoteLength {
    WHOLE("whole", 16),
    HALF("half", 8),
    QUARTER("quarter", 4),
    EIGHTH("eighth", 2),
    SIXTEENTH("16th", 1);

    private final String parsedName;
    private final int duration;

    NoteLength(String parsedName, int duration) {
        this.parsedName = parsedName;
        this.duration = duration;
    }

    public static NoteLength tryParse(String name) {
        return Arrays.stream(NoteLength.values())
            .filter(length -> length.parsedName.equalsIgnoreCase(name))
            .findFirst()
            .orElse(null);
    }

    public int getDuration() {
        return duration;
    }

    public void addDurationAndTypeToNote(Note note) {
        note.setDuration(BigDecimal.valueOf(duration));
        NoteType type = new NoteType();
        type.setValue(parsedName);
        note.setType(type);
    }

    @Override
    public String toString() {
        return "NoteLength{" +
                "parsedName='" + parsedName + '\'' +
                ", duration=" + duration +
                '}';
    }
}
