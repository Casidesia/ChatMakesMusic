package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.ParsedNoteOrRest;
import com.casidesia.chatmakesmusic.util.LogFactory;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.ObjectFactory;
import org.audiveris.proxymusic.ScorePartwise;
import org.audiveris.proxymusic.Time;

import java.util.logging.Logger;

public class ScoreBuilder {
    private static final ObjectFactory factory = new ObjectFactory();
    private static final Logger log = LogFactory.getLoggerForClass(ScoreBuilder.class);

    private static class Constants {
        private static final String SONG_TITLE = "Chats Song";
        private static final String COMPOSER = "The Composer";
    }

    private final ScorePartwise score = new ScorePartwise();



    public void setTimeSignature(Time timeSignature) {
        // Take current measure, get attributes, set time signature
    }

    public void addNote(ParsedNoteOrRest parsedNote) {
        Note note = parsedNote.toXmlNote();

        // Add to measure, etc. etc.
    }

    public ScorePartwise getScore() {
        return score;
    }
}
