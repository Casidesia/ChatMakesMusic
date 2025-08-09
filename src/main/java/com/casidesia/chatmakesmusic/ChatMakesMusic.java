package com.casidesia.chatmakesmusic;

import com.casidesia.chatmakesmusic.data.ParseResult;
import org.audiveris.proxymusic.util.Marshalling;

import java.io.IOException;

public class ChatMakesMusic {
    public static void main(String[] args) throws IOException, Marshalling.MarshallingException {
        ParseResult parseResult = new StreamMusicParser(args[0]).parseFile();
        ScoreBuilder scoreBuilder = new ScoreBuilder();
        scoreBuilder.setTimeSignature(parseResult.timeSignatureUpper(), parseResult.timeSignatureLower());
        parseResult.notes().forEach(scoreBuilder::addNote);
        new ChatsSongPrinter("output/testFile" + System.currentTimeMillis() + ".musicxml")
            .printScoreToFile(scoreBuilder.getScore());
    }
}
