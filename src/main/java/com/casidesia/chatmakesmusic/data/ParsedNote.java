package com.casidesia.chatmakesmusic.data;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.Note;
import org.audiveris.proxymusic.Pitch;
import org.audiveris.proxymusic.Step;

public record ParsedNote(NoteLength length, Step step, int octave) implements ParsedNoteOrRest {
    @Override
    public Note toXmlNote() {
        Note note = new Note();
        length.addDurationAndTypeToNote(note);
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
