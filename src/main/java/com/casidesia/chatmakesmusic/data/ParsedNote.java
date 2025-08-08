package com.casidesia.chatmakesmusic.data;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.Pitch;
import org.audiveris.proxymusic.Step;

import java.math.BigDecimal;

public record ParsedNote(NoteLength length, Step step, int octave) implements ParsedNoteOrRest {
    @Override
    public Note toXmlNote() {
        Note note = new Note();
        note.setDuration(BigDecimal.valueOf(length.getDuration()));
        Pitch pitch = new Pitch();
        pitch.setStep(step);
        pitch.setOctave(octave);
        note.setPitch(pitch);
        return note;
    }

    @Override
    public int getDuration() {
        return length.getDuration();
    }
}
