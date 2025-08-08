package com.casidesia.chatmakesmusic.enums;

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

    public String getParsedName() {
        return parsedName;
    }

    public int getDuration() {
        return duration;
    }
}
