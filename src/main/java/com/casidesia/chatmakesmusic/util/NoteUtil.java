package com.casidesia.chatmakesmusic.util;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.EmptyPlacement;
import org.audiveris.proxymusic.Note;

import java.math.BigDecimal;

public class NoteUtil {
    public static void changeNoteDuration(Note note, int newDuration) {
        if (note.getDuration().intValue() == newDuration)
            return;

        NoteLength newLength = NoteLength.findNextShortestNoteLength(newDuration);
        note.getType().setValue(newLength.getText());
        if (newLength.getDuration() < newDuration)
            note.getDot().add(new EmptyPlacement());
        note.setDuration(BigDecimal.valueOf(newDuration));
    }
}
