package de.pinyin4j.test;

import org.junit.BeforeClass;
import org.junit.Test;

import de.pinyin4j.ParseException;
import de.pinyin4j.PinyinParser;

import static org.junit.Assert.*;

/**
 * Tests the {@link PinyinParser}.
 */
public final class PinyinNormalizationTest {

    // tones: '\u0304', '\u0301', '\u030C', '\u0300'

    private static PinyinParser normalizer;

    /**
     * Initializes the {@link PinyinParser}.
     */
    @BeforeClass
    public static void init() {
        normalizer = new PinyinParser();
    }

    @Test
    public void testToneNumbers() throws ParseException {
        assertEquals("ke\u030Cne\u0301ng", normalizer.parsePinyin("ke3neng2").toString());
        assertEquals("ji\u0304ngju\u0300", normalizer.parsePinyin("jing1ju4").toString());
        assertEquals("ma\u0304ma", normalizer.parsePinyin("ma1ma0").toString());
    }

    @Test
    public void testOmittedNeutralTone() throws ParseException {
        assertEquals("ho\u0301ngpu\u0301taojiu\u030C",
            normalizer.parsePinyin("hong2pu2taojiu3").toString());
        assertEquals("pia\u0300oliang", normalizer.parsePinyin("piao4liang").toString());
    }

    @Test
    public void testVCharacter() throws ParseException {
        assertEquals("nü\u030Cre\u0301n", normalizer.parsePinyin("nv3ren2").toString());
        assertEquals("nü\u030Cre\u0301n", normalizer.parsePinyin("nü3ren2").toString());
    }

    @Test
    public void testToneMarkPersistence() throws ParseException {
        assertEquals("hua\u0300jia\u0304", normalizer.parsePinyin("hua\u0300jia\u0304").toString());
        assertEquals("xia\u030coshi\u0301", normalizer.parsePinyin("xia\u030coshi\u0301").toString());
        assertEquals("de", normalizer.parsePinyin("de").toString());
    }

    @Test
    public void testSyllableTransitions() throws ParseException {
        // Note: not real words anymore

        // tun.ga: expected and legal. tung.a: illegal, since tung does not exist.
        assertEquals("tu\u0304nga\u0300", normalizer.parsePinyin("tu\u0304nga\u0300").toString());
        // ban.o: expected and legal. ba.no: illegal, since no does not exist.
        assertEquals("ba\u030Cn\'o\u0300", normalizer.parsePinyin("ba\u030Cn\'o\u0300").toString());
        // check that it sets a separator between ke and ai (ke'ai)
        assertEquals("ke\u030C\'a\u0300i", normalizer.parsePinyin("ke3ai4").toString());

        // r-coda
        assertEquals("e\u0301rqie\u030C", normalizer.parsePinyin("er2qie3").toString());
        // er.ai: expected and legal. e.rai: illegal, since rai does not exist.
        assertEquals("e\u0301r\'a\u0300i", normalizer.parsePinyin("e\u0301r\'ai\u0300").toString());
    }

    @Test
    public void testErhua() throws ParseException {
        assertEquals("wa\u0301nre\u0300", normalizer.parsePinyin("wan2re4").toString());
        assertEquals("wa\u0301nr'a\u0300", normalizer.parsePinyin("wanr2a4").toString());
        assertEquals("ta\u0301ngrde\u0300", normalizer.parsePinyin("tangr2de4").toString());
        assertEquals("wa\u0301nre\u0300", normalizer.parsePinyin("wa\u0301nre\u0300").toString());
        assertEquals("wa\u0301nr'a\u0300", normalizer.parsePinyin("wa\u0301nr'a\u0300").toString());
        assertEquals("ta\u0301ngrde\u0300", normalizer.parsePinyin("ta\u0301ngrde\u0300").toString());
    }

    @Test
    public void testSpaces() throws ParseException {
        // trim leading and trailing spaces, shorten spaces to 1
        assertEquals("da\u030C qiu\u0301", normalizer.parsePinyin("  da3   qiu2 ").toString());

        // omit separator if space is present
        assertEquals("he\u030Cn e\u0300", normalizer.parsePinyin("hen3 e4").toString());
    }

    @Test
    public void testCCCEDICTCompatibility() throws ParseException {
        // we don't want those mostly redundant spaces they put everywhere
        assertEquals("ke\u030Cshi\u0300", normalizer.parsePinyin("ke3 shi4", false).toString());

        // test v char (u:)
        assertEquals("nü\u030C", normalizer.parsePinyin("nu:3").toString());
        assertEquals("lüe\u0300", normalizer.parsePinyin("lu:e4").toString());

        // test that erhua works normally
        assertEquals("hui\u0300r", normalizer.parsePinyin("hui4 r5", false).toString());
        assertEquals("nür", normalizer.parsePinyin("nu:5 r5", false).toString());
        assertEquals("wa\u0301nr", normalizer.parsePinyin("wan2 r5", false).toString());
        assertEquals("hua\u0304rjia\u0300ng",
            normalizer.parsePinyin("hua1 r5 jiang4", false).toString());

        // test that other chars they put in there are removed
        assertEquals("Zhe\u0304n'A\u0300osi\u0304ti\u0304ng",
            normalizer.parsePinyin("Zhen1 \u00b7 Ao4 si1 ting1", false).toString());
        assertEquals("re\u0301nwe\u0300ica\u0301isi\u030Cnia\u030Cowe\u0300ishi\u0301wa\u0301ng",
            normalizer.parsePinyin("ren2 wei4 cai2 si3 , niao3 wei4 shi2 wang2", false).toString());
    }

    @Test(expected = ParseException.class)
    public void testInvalidSyllable() throws ParseException {
        normalizer.parsePinyin("fai1");
    }

    @Test(expected = ParseException.class)
    public void testOverridingToneDefinitions() throws ParseException {
        normalizer.parsePinyin("ge\u0304n2");
    }

    @Test(expected = ParseException.class)
    public void testInvalidChar() throws ParseException {
        normalizer.parsePinyin("gai2+hello");
    }

    @Test(expected = ParseException.class)
    public void testInvalidSeparator() throws ParseException {
        normalizer.parsePinyin("ke'yi");
    }
}