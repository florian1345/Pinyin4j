// TODO document this file

package de.pinyin4j.test;

import org.junit.Test;

import java.io.IOException;

import de.pinyin4j.ParseException;
import de.pinyin4j.PinyinParser;
import de.pinyin4j.PinyinString;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.fail;

public final class PinyinStringTest {

    private static final PinyinParser PARSER = new PinyinParser();

    private void test(String rawPinyin) {
        try {
            final PinyinString s = PARSER.parsePinyin(rawPinyin);
            IOConsistencyTest.run((toSave, stream) -> s.save(stream), PinyinString::read,
                (s1, s2) -> s1.toString().equals(s2.toString()), s);
        }
        catch (IOException | ParseException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testIOConsistency() {
        test("Wo3 jin1tian1 you3 yi2huir4 ni3 ne ");
        test("zhe4shi4wo3peng2youta1shi4ji4zhe3");
        test("yin1wei4ni3desu4she4tai4xiao3");
        test("suo3yi3wo3menbu4chang2chang2neng2lai2nar4");
    }

    @Test
    public void testEquals() throws ParseException {
        assertEquals(PARSER.parsePinyin(" Ni3 de Su4she4  zai4 nar3"),
            PARSER.parsePinyin("ni3 de0 Su4she4 zai4na3 r5"));
        assertNotEquals(PARSER.parsePinyin("Nar4 you3 ben3zi ma5"),
            PARSER.parsePinyin("Nar4 you2 ben3 zi ma"));
        assertNotEquals(PARSER.parsePinyin("Hui4"), PARSER.parsePinyin("hu4"));
        assertNotEquals(PARSER.parsePinyin("xi4zhu3ren4"), PARSER.parsePinyin("xi4zhu4ren4"));
    }
}
