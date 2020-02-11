package de.pinyin4j;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * An enumeration of all possible initials of a syllable including the empty initial.
 */
public enum Initial {

    /**
     * The empty initial, which is a special case. It is sometimes pronounced /\u0294/ or may result
     * in a glide onset, depending on the {@link Final}. Examples are \u997f and \u6709.
     */
    EMPTY(0, "", Place.EMPTY),

    /**
     * The "b"-initial, which is a voiceless plain bilabial stop (/p/). Since its place of
     * articulation is {@link Place#LABIAL}, it entails different orthography for the final
     * {@link Final#UO}, where "u" is omitted. Examples are \u628a and \u6ce2.
     */
    B(1, "b", Place.LABIAL),

    /**
     * The "p"-initial, which is a voiceless aspirated bilabial stop (/p\u02b0/). Since its place of
     * articulation is {@link Place#LABIAL}, it entails different orthography for the final
     * {@link Final#UO}, where "u" is omitted. Examples are \u76d8 and \u62cd.
     */
    P(2, "p", Place.LABIAL),

    /**
     * The "m"-initial, which is a voiced bilabial nasal (/m/). Examples are \u5417 and \u6c11.
     * Since it is in the class {@link Place#LABIAL}, it entails different orthography for the final
     * {@link Final#UO}, where "u" is omitted.
     */
    M(3, "m", Place.LABIAL),

    /**
     * The "f"-initial, which is a voiceless labiodental fricative (/f/). Examples are \u6cd5 and
     * \u5206. Since its place of articulation is {@link Place#LABIAL}, it entails different
     * orthography for the final {@link Final#UO}, where "u" is omitted.
     */
    F(4, "f", Place.LABIAL),

    /**
     * The "d"-initial, which is a voiceless plain alveolar stop (/t/). Examples are \u591a and
     * \u4f46.
     */
    D(5, "d", Place.OTHER),

    /**
     * The "t"-initial, which is a voiceless aspirated alveolar stop (/t\u02b0/). Examples are
     * \u4ed6 and \u5929.
     */
    T(6, "t", Place.OTHER),

    /**
     * The "n"-initial, which is a voiced alveolar nasal (/n/). Examples are \u90a3 and \u5e74.
     */
    N(7, "n", Place.OTHER),

    /**
     * The "l"-initial, which is a voiced alveolar lateral approximant (/l/). Examples are \u4e86
     * and \u7eff.
     */
    L(8, "l", Place.OTHER),

    /**
     * The "g"-initial, which is a voiceless plain velar stop (/k/). Examples are \u4e2a and \u9986.
     */
    G(9, "g", Place.OTHER),

    /**
     * The "k"-initial, which is a voiceless aspirated velar stop (/k\u02b0/). Examples are \u8bfe
     * and \u770b.
     */
    K(10, "k", Place.OTHER),

    /**
     * The "h"-initial, which is a voiceless velar fricative (/x/). Examples are \u548c and \u5f88.
     */
    H(11, "h", Place.OTHER),

    /**
     * The "j"-initial, which is a voiceless plain palatal affricate (/c\u0361\u0255/). Examples are
     * \u89c1 and \u5267. Since its place of articulation is {@link Place#PALATAL}, it entails
     * different orthography for all "\u00fc"-finals, where "\u00fc" is written "u".
     */
    J(12, "j", Place.PALATAL),

    /**
     * The "q"-initial, which is a voiceless aspirated palatal affricate (/c\u0361\u0255\u02b0/).
     * Examples are \u53bb and \u94b1. Since its place of articulation is {@link Place#PALATAL}, it
     * entails different orthography for all "\u00fc"-finals, where "\u00fc" is written "u".
     */
    Q(13, "q", Place.PALATAL),

    /**
     * The "x"-initial, which is a voiceless palatal fricative (/\u0255/). Examples are \u897f and
     * \u5b66. Since its place of articulation is {@link Place#PALATAL}, it entails different
     * orthography for all "\u00fc"-finals, where "\u00fc" is written "u".
     */
    X(14, "x", Place.PALATAL),

    /**
     * The "zh"-initial, which is a voiceless plain retroflex affricate (/\u0288\u0361\u0282/).
     * Examples are \u4e2d and \u53ea. Since it is a retroflex consonant, combining it with
     * {@link Final#I} results in the nucleus being a syllabic voiced retroflex approximant
     * (/\u027b\u0329/).
     */
    ZH(15, "zh", Place.OTHER),

    /**
     * The "ch"-initial, which is a voiceless aspirated retroflex affricate
     * (/\u0288\u0361\u0282\u02b0/). Examples are \u5e38 and \u5403. Since it is a retroflex
     * consonant, combining it with {@link Final#I} results in the nucleus being a syllabic voiced
     * retroflex approximant (/\u027b\u0329/).
     */
    CH(16, "ch", Place.OTHER),

    /**
     * The "sh"-initial, which is a voiceless retroflex fricative (/\u0282/). Examples are \u4e0a
     * and \u662f. Since it is a retroflex consonant, combining it with {@link Final#I} results in
     * the nucleus being a syllabic voiced retroflex approximant (/\u027b\u0329/).
     */
    SH(17, "sh", Place.OTHER),

    /**
     * The "r"-initial, which is a voiced retroflex approximant (/\u027b/). Examples are \u4eba and
     * \u65e5. This initial is a special case when combined with {@link Final#I}, in which case the
     * entire syllable becomes a syllabic voiced retroflex approximant (/\u027b\u0329/).
     */
    R(18, "r", Place.OTHER),

    /**
     * The "z"-initial, which is a voiceless plain alveolar affricate (/t\u0361s/). Examples are
     * \u5728 and \u5b50. Since it is an alveolar sibilant, combining it with {@link Final#I}
     * results in the nucleus being a syllabic alveolar approximant (/\u0279\u0329/).
     */
    Z(19, "z", Place.OTHER),

    /**
     * The "c"-initial, which is a voiceless aspirated alveolar affricate (/t\u0361s\u02b0).
     * Examples are \u4ece and \u8bcd. Since it is an alveolar sibilant, combining it with
     * {@link Final#I} results in the nucleus being a syllabic alveolar approximant
     * (/\u0279\u0329/).
     */
    C(20, "c", Place.OTHER),

    /**
     * The "s"-initial, which is a voiceless alveolar fricative (/s/). Examples are \u4e09 and
     * \u56db. Since it is an alveolar sibilant, combining it with {@link Final#I} results in the
     * nucleus being a syllabic alveolar approximant (/\u0279\u0329/).
     */
    S(21, "s", Place.OTHER);

    private static Initial[] initials = new Initial[22];
    private static HashMap<String, Initial> bySpelling = new HashMap<>(47);

    private int m_index;
    private String m_spelling;
    private Place m_place;

    Initial(int index, String spelling, Place place) {
        m_index = index;
        m_spelling = spelling;
        m_place = place;
    }

    /**
     * Gets a unique index of this initial.
     *
     * @return A unique index of this initial.
     */
    public int getIndex() {
        return m_index;
    }

    /**
     * Gets the spelling of this initial. For {@link Initial#EMPTY}, this will be an empty
     * string.
     *
     * @return A {@link String} containing the spelling of this initial.
     */
    public String getSpelling() {
        return m_spelling;
    }

    /**
     * Gets the place of articulation of this initial.
     *
     * @return The {@link Place} of articulation.
     */
    public Place getPlace() {
        return m_place;
    }

    /**
     * Looks up the initial associated with the given index. In particular, finds an initial
     * such that {@link Initial#getIndex()} returns <tt>index</tt>.
     *
     * @param index The index to look for.
     * @return The initial associated with the given index.
     * @throws ArrayIndexOutOfBoundsException If there is no initial with the given index.
     */
    public static Initial fromIndex(int index) {
        return initials[index];
    }

    /**
     * Looks up the initial that has the given spelling. In particular, finds an initial such
     * that {@link Initial#getSpelling()} returns <tt>spelling</tt>.
     *
     * @param spelling A {@link String} containing the spelling to look for.
     * @return The initial that has the given spelling.
     * @throws NoSuchElementException If there is no initial with the given spelling.
     */
    public static Initial fromSpelling(String spelling) {
        final Initial result = bySpelling.get(spelling);
        if (result == null)
            throw new NoSuchElementException(
                "There is no initial with spelling \"" + spelling + "\".");
        return result;
    }

    static {
        for (Initial initial : values()) {
            initials[initial.getIndex()] = initial;
            bySpelling.put(initial.getSpelling(), initial);
        }
    }

    /**
     * An enumeration of some places of articulation of the initials which are relevant for
     * orthography. All places that do not have special rules are grouped under
     * {@link Place#OTHER}. The empty initial is also represented here.
     */
    public enum Place {

        /**
         * This is not really a place of articulation, but a category for the empty initial
         * ({@link Initial#EMPTY}).
         */
        EMPTY,

        /**
         * Labial initials are made by pressing the lips together (such as in /m/, /p/, and
         * /p\u02b0), which are bilabial consonants, or pressing the lower lip to the upper teeth
         * (such as /f/), which are labiodentals. In pinyin, they are written as "m", "b", p", and
         * "f" ({@link Initial#M}, {@link Initial#B}, {@link Initial#P}, {@link Initial#F}).
         */
        LABIAL,

        /**
         * Palatal initials are made by raising the body of the tongue to the middle of the mouth's
         * roof. Since glides are attributed to the final in this analysis, the palatal approximant
         * /j/, which is commonly written "y" in pinyin, is not considered part of the initial.
         * Instead, a syllable that starts with "y" has an empty initial. The palatal initials are
         * usually written as "j", "q", and "x" in pinyin ({@link Initial#J}, {@link Initial#Q},
         * {@link Initial#X}).
         */
        PALATAL,

        /**
         * A catch-all for places of articulation that do not require special rules.
         */
        OTHER;
    }
}
