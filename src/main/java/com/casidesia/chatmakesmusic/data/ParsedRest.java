package com.casidesia.chatmakesmusic.data;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.Rest;

import java.math.BigDecimal;

public record ParsedRest(NoteLength length) implements ParsedNoteOrRest {
    @Override
    public Note toXmlNote() {
        Note note = new Note();
        note.setDuration(BigDecimal.valueOf(length.getDuration()));
        note.setRest(new Rest());
        return note;
    }

    @Override
    public int getDuration() {
        return length.getDuration();
    }
}
