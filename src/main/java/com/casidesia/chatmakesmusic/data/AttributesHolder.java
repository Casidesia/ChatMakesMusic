package com.casidesia.chatmakesmusic.data;

import com.casidesia.chatmakesmusic.enums.NoteLength;
import org.audiveris.proxymusic.*;

import java.lang.String;
import java.math.BigDecimal;
import java.math.BigInteger;

public class AttributesHolder {
    private static final ObjectFactory factory = new ObjectFactory();

    private ClefSign clefSign;
    private int keyFifths;
    private int timeSignatureUpper;
    private int timeSignatureLower;
    private int divisions;

    boolean clefChanged;
    boolean keyChanged;
    boolean timeChanged;

    public void setClefSign(ClefSign clefSign) {
        this.clefSign = clefSign;
        clefChanged = true;
    }

    public void setKeyFifths(int keyFifths) {
        this.keyFifths = keyFifths;
        keyChanged = true;
    }

    public void setTime(int timeSignatureUpper, int timeSignatureLower) {
        this.timeSignatureUpper = timeSignatureUpper;
        this.timeSignatureLower = timeSignatureLower;

        // This formula basically converts the original time signature into a ratio, and multiplies it by the number
        // of divisions that a whole note accounts for.
        // The order of operations here is important! Division should happen last, or else the result could be rounded
        // down incorrectly!
        divisions = NoteLength.WHOLE.getDuration() * timeSignatureUpper / timeSignatureLower;

        timeChanged = true;
    }

    public int getDivisions() {
        return divisions;
    }

    public Attributes getCurrentAttributes() {
        Attributes attributes = factory.createAttributes();

        if (clefChanged) {
            Clef clef = factory.createClef();
            clef.setSign(clefSign);
            attributes.getClef().add(clef);
            clefChanged = false;
        }

        if (keyChanged) {
            Key key = factory.createKey();
            key.setFifths(BigInteger.valueOf(keyFifths));
            attributes.getKey().add(key);
            keyChanged = false;
        }

        if (timeChanged) {
            Time timeSignature = factory.createTime();
            timeSignature.getTimeSignature().add(factory.createTimeBeats(String.valueOf(timeSignatureUpper)));
            timeSignature.getTimeSignature().add(factory.createTimeBeatType(String.valueOf(timeSignatureLower)));
            attributes.getTime().add(timeSignature);
            timeChanged = false;
        }

        attributes.setDivisions(BigDecimal.valueOf(divisions));

        return attributes;
    }
}
