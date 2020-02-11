package de.pinyin4j.test;

import org.junit.Test;

import de.pinyin4j.Final;
import de.pinyin4j.Initial;
import de.pinyin4j.Syllable;
import de.pinyin4j.Tone;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link Syllable}s.
 */
public final class SyllableTest {

	// TODO document
    @Test
    public void testValidConstruction() {
        final Syllable a = new Syllable(Initial.N, Final.V, Tone.LOW, false, false),
            b = new Syllable(Initial.CH, Final.UN, Tone.HIGH, false, true),
            c = new Syllable(Initial.EMPTY, Final.UAN, Tone.RISING, true, false),
            d = new Syllable(Initial.N, Final.A, Tone.FALLING, true, true);
        assertEquals(a.getInitial(), Initial.N);
        assertEquals(b.getFinal(), Final.UN);
        assertEquals(c.getTone(), Tone.RISING);
        assertTrue(d.hasRFinal());
        assertTrue(b.isCapitalized());
        assertFalse(a.hasRFinal());
        assertFalse(c.isCapitalized());
    }

    private void assertDataEqual(Syllable a, Syllable b) {
        assertEquals(a.getInitial(), b.getInitial());
        assertEquals(a.getFinal(), b.getFinal());
        assertEquals(a.getTone(), b.getTone());
        assertEquals(a.hasRFinal(), b.hasRFinal());
        assertEquals(a.isCapitalized(), b.isCapitalized());
    }

	// TODO document
    @Test
    public void testShortConversion() {
        final Syllable a = new Syllable(Initial.ZH, Final.E, Tone.FALLING, true, false),
            b = new Syllable(Initial.EMPTY, Final.UANG, Tone.RISING, false, true),
            a2 = Syllable.fromShort(a.toShort()),
            b2 = Syllable.fromShort(b.toShort());
        assertDataEqual(a, a2);
        assertDataEqual(b, b2);
    }
}
