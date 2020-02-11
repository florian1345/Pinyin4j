package de.pinyin4j;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import static de.pinyin4j.util.Util.hashSetOf;

/**
 * A class that accepts strings representing pinyin with tones written as numbers after syllables
 * and converts it into standard pinyin with diacritics.
 */
public final class PinyinParser {

    private static final Character[] TONE_MARKS_ARRAY =
        new Character[] { '\u0304', '\u0301', '\u030C', '\u0300' };
    private static final HashSet<Character> CONSONANTS =
        hashSetOf('b', 'p', 'm', 'f', 'd', 't', 'n', 'l', 'z', 'c', 's', 'r', 'j', 'q', 'x', 'g',
            'k', 'h', 'B', 'P', 'M', 'F', 'D', 'T', 'N', 'L', 'Z', 'C', 'S', 'R', 'J', 'Q', 'X',
            'G', 'K', 'H');
    private static final HashSet<Character> GLIDES = hashSetOf('y', 'w', 'Y', 'W');
    static final HashSet<Character> VOWELS =
        hashSetOf('a', 'o', 'e', 'i', 'u', '\u00fc', 'v', 'A', 'O', 'E', 'I', 'U', '\u00dc', 'V');
    private static final HashSet<Character> TONES =
        hashSetOf('0', '1', '2', '3', '4', '5');
    private static final HashSet<Character> TONE_MARKS = hashSetOf(TONE_MARKS_ARRAY);

    private String m_toParse;
    private int m_index = 0;
    private StringBuilder m_sb = new StringBuilder();

    /**
     * Creates a new pinyin normalizer.
     */
    public PinyinParser() { }

    private void appendWhileInSet(Set<Character> charset) throws ParseException {
        while (charset.contains(currChar())) {
            m_sb.append(acceptIt());
        }
    }

    private void appendVowels() throws ParseException {
        while (VOWELS.contains(currChar())) {
            final char c = acceptIt();
            if (c == 'v') m_sb.append('\u00fc');
            else if (c == 'u' && currChar() == ':') { // CC-CEDICT system (u: = ü)
                m_sb.append('\u00fc');
                acceptIt();
            }
            else m_sb.append(c);
        }
    }

    private int parseToneMark() throws ParseException {
        if (TONE_MARKS.contains(currChar())) {
            final int toneMark = acceptIt();

            for (int i = 0; i < TONE_MARKS_ARRAY.length; i++) {
                if (toneMark == TONE_MARKS_ARRAY[i])
                    return i + 1;
            }
        }

        return 0;
    }

    private boolean handleCodaChar(char expectedCodaChar) throws ParseException {
        final char next = lookahead(1);

        if (currChar() == expectedCodaChar &&
                (!VOWELS.contains(next) || next == '\'' || next == '\u0003'))  {
            m_sb.append(acceptIt());
            return true;
        }

        return false;
    }

    private Syllable parseNormalSyllable() throws ParseException {
        m_sb.setLength(0);

        // initial consonant
        appendWhileInSet(CONSONANTS);
        final String initialSpelling = m_sb.toString();
        Initial initial = null;
        try {
            initial = Initial.fromSpelling(initialSpelling.toLowerCase());
        }
        catch (NoSuchElementException e) {
            throw new ParseException("Invalid initial: " + initialSpelling);
        }

        // nucleus with tone
        appendWhileInSet(GLIDES);
        appendVowels();
        int tone = parseToneMark();
        if (tone != 0) appendVowels();

        // at this point we have to have something, otherwise the character is invalid.
        if (m_sb.length() == 0)
            throw new ParseException("Unexpected character: \"" + currChar() + "\".");

        // syllable final
        if (handleCodaChar('n'))
            handleCodaChar('g');
        final String finalSpelling = m_sb.substring(initialSpelling.length());
        Final final_ = null;
        try {
            final_ = Final.fromSpelling(initial, finalSpelling.toLowerCase());
        }
        catch (NoSuchElementException e) {
            throw new ParseException("Invalid final: " + finalSpelling + " (after initial \"" +
                initial.getSpelling() + "\".");
        }

        // handle capitalization
        final boolean capitalized = Character.isUpperCase(m_sb.charAt(0));

        // handle r final
        final boolean rFinal = currChar() == 'r' && !VOWELS.contains(lookahead(1));
        if (rFinal) acceptIt();
        if (tone == 0) tone = parseTone();

        try {
            return new Syllable(initial, final_, Tone.fromIndex(tone), rFinal, capitalized);
        }
        catch (IllegalArgumentException e) {
            throw new ParseException(e.getMessage());
        }
    }

    private int parseTone() throws ParseException {
        if (!TONES.contains(currChar()))
            return 0; //throw new ParseException("Invalid tone number: " + currChar() + ".");
        return (acceptIt() - '0') % 5; // 5 for neutral tone accepted
    }

    private Syllable parseSyllable() throws ParseException {
        final Syllable normalSyllable = parseNormalSyllable();

        if (currChar() == '\'') {
            acceptIt();
            if (!VOWELS.contains(currChar()))
                throw new ParseException("Unexpected syllable separator after syllable \"" +
                    normalSyllable.toString() + "\".");
        }

        return normalSyllable;
    }

    private boolean skipIgnored() throws ParseException {
        boolean result = false;

        // ignore whitespace, centerdot and comma which are used as separators in CEDICT
        while (Character.isWhitespace(currChar()) || currChar() == '\u00b7' || currChar() == ',') {
            acceptIt();
            result = true;
        }

        return result;
    }

    // parses "r5" if present, which is the CC-CEDICT representation of r finals
    private boolean parseRFinal() throws ParseException {
        if (currChar() == 'r' && lookahead(1) == '5') {
            acceptIt();
            acceptIt();
            return true;
        }

        return false;
    }

    /**
     * Parses the given string to a pinyin string. By default, whitespace is parsed and added to
     * the result. If you want whitespace to be removed, call
     * {@link PinyinParser#parsePinyin(String, boolean)} with the appropriate arguments. This is
     * also CC-CEDICT compatible.
     *
     * @param pinyin A {@link String} containing a pinyin text with tones being represented by
     *  numbers (0 for neutral tone) or marks and optionally 'v' or 'u:' for 'ü'. Retroflex finals
     *  may be added to the syllable (e.g. "huir4") or added as an extra segment spelled "r5" (e.g.
     *  "hui4 r5"). Must contain a whole number of syllables.
     * @return A {@link PinyinString} containing a normalized pinyin word with the pronunciation
     *  specified by the unformatted input.
     * @throws ParseException If the given input is not correct pinyin.
     */
    public PinyinString parsePinyin(String pinyin) throws ParseException {
        return parsePinyin(pinyin, true);
    }

    /**
     * Parses the given string to a pinyin string. This is also CC-CEDICT compatible.
     *
     * @param pinyin A {@link String} containing a pinyin text with tones being represented by
     *  numbers (0 for neutral tone) or marks and optionally 'v' or 'u:' for 'ü'. Retroflex finals
     *  may be added to the syllable (e.g. "huir4") or added as an extra segment spelled "r5" (e.g.
     *  "hui4 r5"). Must contain a whole number of syllables.
     * @param parseWhitespace Indicates, whether the resulting string contains whitespace
     *   (represented as <tt>null</tt> syllables).
     * @return A {@link PinyinString} containing a normalized pinyin word with the pronunciation
     *  specified by the unformatted input.
     * @throws ParseException If the given input is not correct pinyin.
     */
    public PinyinString parsePinyin(String pinyin, boolean parseWhitespace) throws ParseException {
        m_toParse = pinyin.trim();
        m_index = 0;
        final int length = m_toParse.length();
        final List<Syllable> syllables = new ArrayList<>();

        while (m_index < length) {
            if (parseRFinal()) {
                if (syllables.size() == 0)
                    throw new ParseException("Invalid r final at the beginning.");
                int lastIdx = syllables.size() - 1;
                Syllable last = syllables.get(lastIdx);
                if (last == null) {
                    lastIdx--;
                    last = syllables.get(lastIdx);
                }
                if (last.hasRFinal())
                    throw new ParseException("Double r-final.");
                syllables.set(lastIdx, new Syllable(last.getInitial(), last.getFinal(),
                    last.getTone(), true, last.isCapitalized()));
            }
            else {
                final Syllable syllable = parseSyllable();
                syllables.add(syllable);
            }

            if (skipIgnored() && m_index < length && parseWhitespace)
                syllables.add(null);
        }

        return new PinyinString(syllables);
    }

    private char acceptIt() throws ParseException {
        final char result = currChar();
        if (result == '\u0003')
            throw new ParseException("Unexpected ending.");
        m_index++;
        return result;
    }

    private char currChar() {
        return lookahead(0);
    }

    private char lookahead(int amount) {
        final int totalIdx = m_index + amount;
        if (totalIdx >= m_toParse.length()) return '\u0003';
        return m_toParse.charAt(totalIdx);
    }
}
