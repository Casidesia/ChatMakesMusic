package com.casidesia.chatmakesmusic.data;

import java.util.List;

public record ParseResult(int timeSignatureUpper, int timeSignatureLower, List<ParsedNoteOrRest> notes) {
}
