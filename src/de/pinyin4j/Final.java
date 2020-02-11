package de.pinyin4j;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * An enumeration of all possible finals of a syllable.
 */
public enum Final {

    /**
     * The "a"-final. It is pronounced /a/. Examples are \u4ed6 and \u554a.
     */
    A(0, false, false, "a"),

    /**
     * The "o"-final. It is pronounced /o/. Examples are \u54af and \u54e6. Note that it is not the
     * final used in syllables like "mo", since that is {@link Final#UO}. To avoid collisions with
     * spelling, calling {@link Final#getSpelling(Initial)} with any initial whose place is
     * {@link Initial.Place#LABIAL} will return "_".
     */
    O(1, false, false, "o", "_", "o", "o"),

    /**
     * The "e"-final. It is pronounced /\u0264/. Examples are \u559d and \u997f. It is also used
     * to construct the "er"-syllable, for which a syllable with {@link Syllable#hasRFinal()},
     * {@link Initial#EMPTY} and ths final has to be created.
     */
    E(2, false, false, "e"),

    /**
     * The "ai"-final. It is pronounced /ai\u032f/. Examples are \u4e70 and \u7231.
     */
    AI(3, false, false, "ai"),

    /**
     * The "ei"-final. It is pronounced /ei\u032f/. Examples are \u7ed9 and \u6b38.
     */
    EI(4, false, false, "ei"),

    /**
     * The "ao"-final. It is pronounced /au\u032f/. Examples are \u597d and \u5965.
     */
    AO(5, false, false, "ao"),

    /**
     * The "ou"-final. It is pronounced /ou\u032f/. Examples are \u90fd and \u6b27.
     */
    OU(6, false, false, "ou"),

    /**
     * The "an"-final. It is pronounced /an/. Examples are \u4e09 and \u5b89.
     */
    AN(7, false, true, "an"),

    /**
     * The "en"-final. It is pronounced /\u0259n/. Examples are \u5206 and \u6069.
     */
    EN(8, false, true, "en"),

    /**
     * The "ang"-final. It is pronounced /a\u014b/. Examples are \u957f and \u6602.
     */
    ANG(9, false, true, "ang"),

    /**
     * The "eng"-final. It is pronounced /\u0259\u014b/. Examples are \u80fd and \u97a5.
     */
    ENG(10, false, true, "eng"), // "er" is omitted, it is constructed using e + r. er + r would be strange

    /**
     * The "i"-final (spelled "yi" in isolation). It is pronounced /i/. Examples are \u4f60 and
     * \u4e00. This also encompasses syllables in which it is pronounced as a syllabic
     * consonant. These are /\u0279\u0329/ in "zi", "ci", and "si", or /\u027b\u0329/ in "zhi",
     * "chi", "shi", and "ri". Since syllabic consonants and true "i"-finals are mutually
     * exclusive with regard to the initials, they can be merged into an orthographic "i"-final.
     */
    I(11, true, false, "yi", "i"),

    /**
     * The "ia"-final (spelled "ya" in isolation). It is pronounced /ja/. Examples are \u4e0b
     * and \u9e2d.
     */
    IA(12, true, false, "ya", "ia"),

    /**
     * The "io"-final (spelled "yo" in isolation). In fact, it only occurs in isolation. It is
     * pronounced /jo/. An example is \u5537.
     */
    IO(13, true, false, "yo", "io"), // no initial matches, but it would be spelled io in that case

    /**
     * The "ie"-final (spelled "ye" in isolation). It is pronounced /je/. Examples are \u522b
     * and \u4e5f.
     */
    IE(14, true, false, "ye", "ie"),

    /**
     * The "iai"-final (spelled "yai" in isolation). In fact, it only occurs in isolation. It
     * is pronounced /jai\u032f/. An example is \u5d16 in some cases. However, this final is
     * extraordinary.
     */
    IAI(15, true, false, "yai", "iai"), // same as IO

    /**
     * The "iao"-final (spelled "yao" in isolation). It is pronounced /jau\u032f/. Examples are
     * \u5c0f and \u8981.
     */
    IAO(16, true, false, "yao", "iao"),

    /**
     * The "iu"-final (spelled "you" in isolation). It is pronounced /jou\u032f. Examples are \u516d
     * and \u6709.
     */
    IU(17, true, false, "you", "iu"),

    /**
     * The "ian"-final (spelled "yan" in isolation). It is pronounced /j\u025bn/. Examples are
     * \u5929 and \u6f14.
     */
    IAN(18, true, true, "yan", "ian"),

    /**
     * The "in"-final (spelled "yin" in isolation). It is pronounced /in/. Examples are \u65b0
     * and \u97f3.
     */
    IN(19, true, true, "yin", "in"),

    /**
     * The "iang"-final (spelled "yang" in isolation). It is pronounced /ja\u014b/. Examples are
     * \u4e24 and \u6837.
     */
    IANG(20, true, true, "yang", "iang"),

    /**
     * The "ing"-final (spelled "ying" in isolation). It is pronounced /i\u014b/. Examples are
     * \u8bf7 and \u5f71.
     */
    ING(21, true, true, "ying", "ing"),

    /**
     * The "iong"-final (spelled "yong" in isolation). It is pronounced /j\u028a\u014b/.
     * Examples are \u718a and \u7528.
     */
    IONG(22, true, true, "yong", "iong"),

    /**
     * The "u"-final (spelled "wu" in isolation). It is pronounced /u/. Examples are \u4e0d and
     * \u4e94.
     */
    U(23, true, false, "wu", "u"),

    /**
     * The "ua"-final (spelled "wa" in isolation). It is pronounced /wa/. Examples are \u6302
     * and \u74e6.
     */
    UA(24, true, false, "wa", "ua"),

    /**
     * The "uo"-final (spelled "wo" in isolation and "o" after a labial initial). It is
     * pronounced /wo/. Examples are \u591a and \u6211.
     */
    UO(25, true, false, "wo", "o", "uo", "uo"), // does not exist for palatal, but "uo" is selected here

    /**
     * The "uai"-final (spelled "wai" in isolation). It is pronounced /wai\u032f/. Examples are
     * \u5feb and \u5916.
     */
    UAI(26, true, false, "wai", "uai"),

    /**
     * The "ui"-final (spelled "wei" in isolation). It is pronounced /wei\u032f/. Examples are
     * \u5bf9 and \u4e3a.
     */
    UI(27, true, false, "wei", "ui"),

    /**
     * The "uan"-final (spelled "wan" in isolation). It is pronounced /wan/. Examples are \u5173
     * and \u665a.
     */
    UAN(28, true, true, "wan", "uan"),

    /**
     * The "un"-final (spelled "wen" in isolation). It is pronounced /w\u0259n/. Examples are
     * \u6625 and \u95ee.
     */
    UN(29, true, true, "wen", "un"),

    /**
     * The "uang"-final (spelled "wang" in isolation). It is pronounced /wa\u014b/. Examples are
     * \u5e8a and \u738b.
     */
    UANG(30, true, true, "wang", "uang"),

    /**
     * The "ong"-final (spelled "weng" in isolation). These two finals are unified, since
     * neither an isolated "ong" nor a "ueng" with initial exist. It is pronounced
     * /w\u0259\u014b/ in isolation and /\u028a\u014b/ with initial. Examples are \u4e1c and
     * \u7fc1.
     */
    ONG(31, true, true, "weng", "ong"), // added to group u because of "weng"

    /**
     * The "ü"-final (spelled "yu" in isolation and "u" after a palatal initial). It is
     * pronounced /y/. Examples are \u5973 and \u8bed.
     */
    V(32, true, false, "yu", "ü", "u", "ü"), // ü-group finals do not exist for labial, but "ü" is selected here

    /**
     * The "üe"-final (spelled "yue" in isolation and "ue" after a palatal initial). It is
     * pronounced /\u0265e/. Examples are \u5b66 and \u6708.
     */
    VE(33, true, false, "yue", "üe", "ue", "üe"),

    /**
     * The "ün"-final (spelled "yun" in isolation and "un" after a palatal initial). It is
     * pronounced /yn/. Examples are \u519b and \u4e91.
     */
    VN(34, true, true, "yun", "ün", "un", "ün"),

    /**
     * The "üan"-final (spelled "yuan" in isolation and "uan" after a palatal initial). It is
     * pronounced /\u0265\u025bn/. Examples are \u5168 and \u5143.
     */
    VAN(35, true, true, "yuan", "üan", "uan", "üan");

    private static Final[] finals = new Final[36];
    private static HashMap<Initial.Place, HashMap<String, Final>> bySpelling = new HashMap<>(11);

    private int m_index;
    private boolean m_unambiguousIsolatedStart, m_hasCoda;
    private HashMap<Initial.Place, String> m_spellings;

    Final(int index, boolean unambiguousIsolatedStart, boolean hasCoda, String spelling) {
        this(index, unambiguousIsolatedStart, hasCoda, spelling, spelling, spelling, spelling);
    }

    Final(int index, boolean unambiguousIsolatedStart, boolean hasCoda, String emptyInitialSpelling,
            String spelling) {
        this(index, unambiguousIsolatedStart, hasCoda, emptyInitialSpelling, spelling, spelling,
            spelling);
    }

    Final(int index, boolean unambiguousIsolatedStart, boolean hasCoda, String emptyInitialSpelling,
          String labialInitialSpelling, String palatalInitialSpelling, String otherSpelling) {
        m_index = index;
        m_unambiguousIsolatedStart = unambiguousIsolatedStart;
        m_hasCoda = hasCoda;
        m_spellings = new HashMap<>(11);
        m_spellings.put(Initial.Place.EMPTY, emptyInitialSpelling);
        m_spellings.put(Initial.Place.LABIAL, labialInitialSpelling);
        m_spellings.put(Initial.Place.PALATAL, palatalInitialSpelling);
        m_spellings.put(Initial.Place.OTHER, otherSpelling);
    }

    /**
     * Gets the spelling of this final. This may be dependent on the initial, for example the
     * final "ian" is spelled "yan" with empty initial.
     *
     * @param initial The {@link Initial} before this final.
     * @return A {@link String} containing the spelling of this final after the given initial.
     */
    public String getSpelling(Initial initial) {
        return m_spellings.get(initial.getPlace());
    }

    /**
     * Gets a unique index of this final.
     *
     * @return A unique index of this final.
     */
    public int getIndex() {
        return m_index;
    }

    /**
     * Indicates whether this final has an unambiguous start, even if it is isolated. This means
     * that it does not start with a vowel, which could interfere with the coda of the previous
     * syllable.
     *
     * @return <tt>true</tt>, if and only if this final has an unambiguous start.
     */
    public boolean hasUnambiguousIsolatedStart() {
        return m_unambiguousIsolatedStart;
    }

    /**
     * Indicates whether this final has a coda (such as "n" or "ng").
     *
     * @return <tt>true</tt>, if and only if this final has a coda.
     */
    public boolean hasCoda() {
        return m_hasCoda;
    }

    /**
     * Looks up the final associated with the given index. In particular, finds a final such
     * that {@link Final#getIndex()} return <tt>index</tt>.
     *
     * @param index The index to look for.
     * @return The final associated with the given index.
     * @throws ArrayIndexOutOfBoundsException If there is no final with the given index.
     */
    public static Final fromIndex(int index) {
        return finals[index];
    }

    /**
     * Looks up the final that has the given spelling. Since the spelling of a final is
     * dependent on the preceding initial, it also has to be specified. More formally, this
     * method finds an final such that {@link Final#getSpelling(Initial)} called with
     * <tt>initial</tt> returns <tt>spelling</tt>.
     *
     * @param initial The {@link Initial} that is in front of the final to find.
     * @param spelling A {@link String} containing the spelling to look for.
     * @return The final that has the given spelling when it comes after the given initial.
     * @throws NoSuchElementException If there is no final that has the given spelling after the
     *  specified initial.
     */
    public static Final fromSpelling(Initial initial, String spelling) {
        final Final result = bySpelling.get(initial.getPlace()).get(spelling);
        if (result == null)
            throw new NoSuchElementException(
                "There is no final with spelling \"" + spelling + "\" when the initial is \"" +
                initial.getSpelling() + "\".");
        return result;
    }

    static {
        for (Initial.Place place : Initial.Place.values()) {
            bySpelling.put(place, new HashMap<>(73));
        }

        for (Final final_ : values()) {
            finals[final_.getIndex()] = final_;

            for (Map.Entry<Initial.Place, String> spelling : final_.m_spellings.entrySet()) {
                bySpelling.get(spelling.getKey()).put(spelling.getValue(), final_);
            }
        }
    }
}
