// TODO document this file

package de.pinyin4j;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A string of pinyin text. Contains {@link Syllable}s and spaces.
 */
public final class PinyinString {

    private static final byte SPACE = (byte)0x28, TERMINATOR = (byte)0x30;
    public static final boolean SPACE_SENSITIVE = false, CASE_SENSITIVE = false;

    private List<Syllable> m_syllables; // spaces represented by null entries
    private String m_asString = null;

    PinyinString(List<Syllable> syllables) {
        m_syllables = syllables;
    }

    public void save(DataOutputStream writer) throws IOException {
        for (Syllable s : m_syllables) {
            if (s == null) writer.writeByte(SPACE);
            else writer.writeShort(s.toShort());
        }

        writer.writeByte(TERMINATOR);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof PinyinString)) return false;
        final Iterator<Syllable> thisIterator = iterate(m_syllables),
            otherIterator = iterate(((PinyinString)other).m_syllables);

        while (thisIterator.hasNext() && otherIterator.hasNext()) {
            final Syllable thisSyl = thisIterator.next(),
                otherSyl = otherIterator.next();
            if (thisSyl == null && otherSyl == null) continue;
            if (thisSyl == null ^ otherSyl == null) return false;
            if (!thisSyl.equals(otherSyl)) return false;
        }

        return !(thisIterator.hasNext() && otherIterator.hasNext());
    }

    private Iterator<Syllable> iterate(List<Syllable> syllables) {
        return syllables.stream()
            .filter(s -> SPACE_SENSITIVE || s != null).iterator();
    }

    @Override
    public String toString() {
        if (m_asString != null) return m_asString;
        final StringBuilder resultBuilder = new StringBuilder();
        boolean insideWord = false;

        for (Syllable syllable : m_syllables) {
            if (syllable == null) {
                resultBuilder.append(" ");
                insideWord = false;
            }
            else {
                if (insideWord && !syllable.hasUnambiguousStart())
                    resultBuilder.append("\'");
                resultBuilder.append(syllable.toString());
                insideWord = true;
            }
        }

        m_asString = resultBuilder.toString();
        return m_asString;
    }

    public static PinyinString read(DataInputStream reader) throws IOException {
        final List<Syllable> syllables = new ArrayList<>();

        for (byte first = reader.readByte(); first != TERMINATOR; first = reader.readByte()) {
            if (first == SPACE) syllables.add(null);
            else {
                // readByte() returns int, so we have to cut out the byte manually
                short s = (short)(first << 8 | (reader.readByte() & 0xff));
                syllables.add(Syllable.fromShort(s));
            }
        }

        return new PinyinString(syllables);
    }
}
