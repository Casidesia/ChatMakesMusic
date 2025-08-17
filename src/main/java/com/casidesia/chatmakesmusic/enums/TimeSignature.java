package com.casidesia.chatmakesmusic.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public enum TimeSignature {
    TWO_TWO("2/2"),
    TWO_FOUR("2/4"),
    THREE_TWO("3/2"),
    THREE_FOUR("3/4"),
    THREE_EIGHT("3/8"),
    FOUR_TWO("4/2"),
    FOUR_FOUR("4/4"),
    FOUR_EIGHT("4/8"),
    SIX_FOUR("6/4"),
    SIX_EIGHT("6/8"),
    NINE_FOUR("9/4"),
    NINE_EIGHT("9/8"),
    TWELVE_FOUR("12/8"),
    TWELVE_EIGHT("12/8");

    private String text;
    private static final List<TimeSignature> VALUES =
            Collections.unmodifiableList(Arrays.asList(values()));
    private static final int SIZE = VALUES.size();
    private static final Random RANDOM = new Random();

    TimeSignature(String signatureText) {
        this.text = signatureText;
    }
    String getText(){
        return this.text;
    }
    public static String randomTimeSignature()  {
        TimeSignature signature = VALUES.get(RANDOM.nextInt(SIZE));
        return signature.getText();
    }
}
