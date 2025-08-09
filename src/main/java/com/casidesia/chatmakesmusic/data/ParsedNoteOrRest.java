package com.casidesia.chatmakesmusic.data;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.NoteType;

import java.math.BigDecimal;

public interface ParsedNoteOrRest {
    Note toXmlNote();
    int getDuration();
}
