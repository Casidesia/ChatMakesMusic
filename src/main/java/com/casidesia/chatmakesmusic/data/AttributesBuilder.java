package com.casidesia.chatmakesmusic.data;

import org.audiveris.proxymusic.*;

public class AttributesBuilder {
    private static final ObjectFactory factory = new ObjectFactory();

    private Clef clef;
    private Key key;
    private int timeSignatureUpper;
    private int timeSignatureLower;

    boolean clefChanged;
    boolean keyChanged;
    boolean timeChanged;

    public void setClef(Clef clef) {
        this.clef = clef;
        clefChanged = true;
    }

    public void setKey(Key key) {
        this.key = key;
        keyChanged = true;
    }

    public void setTime(int timeSignatureUpper, int timeSignatureLower) {
        this.timeSignatureUpper = timeSignatureUpper;
        this.timeSignatureLower = timeSignatureLower;
        timeChanged = true;
    }

    public Attributes build() {
        Attributes attributes = factory.createAttributes();

        if (clefChanged) {
            // Add clef to attributes
            clefChanged = false;
        }

        if (keyChanged) {
            // Add key to attributes
            keyChanged = false;
        }

        if (timeChanged) {
            // Add time to attributes
            timeChanged = false;
        }

        // Add divisions to attributes

        return attributes;
    }
}
