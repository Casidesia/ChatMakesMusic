package com.casidesia.chatmakesmusic;

import org.audiveris.proxymusic.util.Marshalling;

import java.io.IOException;

public class ChatMakesMusic {


    public static void main(String[] args) throws IOException, Marshalling.MarshallingException {
        new ChatsSongPrinter("output/ChatsSong" + System.currentTimeMillis() + ".musicxml")
            .printScoreToFile(new StreamMusicParser(args[0]).parseFile());
    }
}
