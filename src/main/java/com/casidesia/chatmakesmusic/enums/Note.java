package com.casidesia.chatmakesmusic.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum Note {
    A("A"),
    B("B"),
    C("C"),
    D("D"),
    E("E"),
    F("F"),
    G("G");

    private String text;
    private static final List<Note> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    Note(String note) {
        this.text = note;
    }

    String getText() {
        return this.text;
    }
    public static String randomNote()  {
        Note note = VALUES.get(RANDOM.nextInt(SIZE));
        return note.getText();
    }

}
