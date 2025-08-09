package com.casidesia.chatmakesmusic.data;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import com.casidesia.chatmakesmusic.util.LogFactory;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.Rest;

import java.math.BigDecimal;
import java.util.logging.Logger;

public record ParsedRest(NoteLength length) implements ParsedNoteOrRest {
    private static final Logger log = LogFactory.getLogger();
    @Override
    public Note toXmlNote() {
        Note note = new Note();
        length.addDurationAndTypeToNote(note);
        note.setRest(new Rest());
        return note;
    }

    @Override
    public int getDuration() {
        return length.getDuration();
    }

}
