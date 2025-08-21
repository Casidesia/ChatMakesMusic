package com.casidesia.chatmakesmusic.data;

import org.audiveris.proxymusic.Note;

public interface ParsedNoteOrRest {
    Note toXmlNote();

    int getDuration();
}
