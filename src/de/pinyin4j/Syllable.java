package de.pinyin4j;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A single syllable that can be represented using Pinyin. It also contains information whether it
 * is capitalized.
 */
public class Syllable {

    private static final short INITIAL_MASK = 0x001f;
    private static final short FINAL_SHIFT = 5, FINAL_MASK = 0x07e0;
    private static final short TONE_SHIFT = 11, TONE_MASK = 0x3800;
    private static final short R_FINAL_MASK = 0x4000;
    private static final short CAPITAL_MASK = (short)0x8000;

    private static final HashMap<Initial, HashSet<Final>> LEGAL_COMBINATIONS = new HashMap<>(47);

    static {
        for (Initial initial : Initial.values()) {
            LEGAL_COMBINATIONS.put(initial, new HashSet<Final>(73));
        }

        fillLegalCombinations();
    }

    private static void fillLegalCombinations() {
        // empty or glide
        addCross(arr(Initial.EMPTY), Final.values()); // only danger: ong vs weng, which we unified

        // labials
        addCross(arr(Initial.B, Initial.P, Initial.M, Initial.F),
            Final.A, Final.EI, Final.AN, Final.EN, Final.ANG, Final.ENG, Final.IAO, Final.U,
            Final.UO);
        addCross(arr(Initial.B, Initial.P, Initial.M),
            Final.AI, Final.AO, Final.I, Final.IE, Final.IAN, Final.IN, Final.ING);
        add(Initial.M, Final.E);
        addCross(arr(Initial.P, Initial.M, Initial.F), Final.OU);
        add(Initial.M, Final.IU);
        add(Initial.B, Final.IANG);

        // non-sibilant alveolars
        addCross(arr(Initial.D, Initial.T, Initial.N, Initial.L),
            Final.A, Final.E, Final.AI, Final.EI, Final.AO, Final.OU, Final.AN, Final.ANG,
            Final.ENG, Final.ONG, Final.I, Final.IE, Final.IAO, Final.IAN, Final.ING, Final.U,
            Final.UO, Final.UAN, Final.UN);
        add(Initial.L, Final.O);
        addCross(arr(Initial.D, Initial.N), Final.EN);
        addCross(arr(Initial.D, Initial.N, Initial.L), Final.IA, Final.IU, Final.IANG);
        addCross(arr(Initial.N, Initial.L), Final.IN, Final.V, Final.VE);
        addCross(arr(Initial.D, Initial.T), Final.UI);
        addCross(arr(Initial.L), Final.VAN, Final.VN);

        // velars
        addCross(arr(Initial.G, Initial.K, Initial.H),
            Final.A, Final.E, Final.AI, Final.EI, Final.AO, Final.OU, Final.AN, Final.EN, Final.ANG,
            Final.ENG, Final.ONG, Final.U, Final.UA, Final.UO, Final.UAI, Final.UI, Final.UAN,
            Final.UN, Final.UANG);

        // palatals
        addCross(arr(Initial.J, Initial.Q, Initial.X),
            Final.I, Final.IA, Final.IE, Final.IAO, Final.IU, Final.IAN, Final.IN, Final.ING,
            Final.IANG, Final.IONG, Final.V, Final.VE, Final.VAN, Final.VN);

        // sibilants
        addCross(
            arr(Initial.ZH, Initial.CH, Initial.SH, Initial.R, Initial.Z, Initial.C, Initial.S),
            Final.E, Final.AO, Final.OU, Final.AN, Final.EN, Final.ANG, Final.ENG, Final.ONG,
            Final.I, Final.U, Final.UO, Final.UI, Final.UAN, Final.UN);
        addCross(arr(Initial.ZH, Initial.CH, Initial.SH, Initial.Z, Initial.C, Initial.S),
            Final.A, Final.AI);
        addCross(arr(Initial.ZH, Initial.SH, Initial.Z, Initial.S),
            Final.EI);
        addCross(arr(Initial.ZH, Initial.CH, Initial.SH),
            Final.UA, Final.UAI, Final.UANG);
        add(Initial.R, Final.UA);
    }

    private static void add(Initial i, Final f) {
        LEGAL_COMBINATIONS.get(i).add(f);
    }

    private static void addCross(Initial[] is, Final... fs) {
        for (Initial i : is) {
            LEGAL_COMBINATIONS.get(i).addAll(Arrays.asList(fs));
        }
    }

    private static Initial[] arr(Initial... is) {
        return is;
    }

    private static boolean valid(Initial initial, Final final_) {
        return LEGAL_COMBINATIONS.get(initial).contains(final_);
    }

    private Initial m_initial;
    private Final m_final;
    private Tone m_tone;
    private boolean m_rFinal, m_capitalized;

    /**
     * Creates a new syllable from initial, final, tone, as well as two flags.
     *
     * @param initial The {@link Initial} (onset) of the created syllable.
     * @param final_ The {@link Final} (nucleus + coda + onset in some cases like "yan") of the
     *  created syllable.
     * @param tone The {@link Tone} of the created syllable.
     * @param rFinal Indicates whether this syllable has an (additional) "r"-final. This does not
     *  mean that {@link Syllable#getFinal()} is replaced by "r", but that an "r" is added after the
     *  normal final (e.g. "wanr"). This flag is also used in the creation of the syllable "er".
     * @param capitalized Indicates whether the first character in this syllable is capitalized.
     */
    public Syllable(Initial initial, Final final_, Tone tone, boolean rFinal, boolean capitalized) {
        if (!valid(initial, final_))
            throw new IllegalArgumentException("Initial \"" + initial.getSpelling() + "\" cannot " +
                "be combined with final \"" + final_.toString() + "\".");
        m_initial = initial;
        m_final = final_;
        m_tone = tone;
        m_rFinal = rFinal;
        m_capitalized = capitalized;
    }

    /**
     * Gets the initial (onset) of this syllable.
     *
     * @return The {@link Initial} of this syllable.
     */
    public Initial getInitial() {
        return m_initial;
    }

    /**
     * Gets the final (nucleus + coda + onset in some cases like "yan") of this syllable.
     *
     * @return The {@link Final} of this syllable.
     */
    public Final getFinal() {
        return m_final;
    }

    /**
     * Gets the tone of this syllable.
     *
     * @return The {@link Tone} of this syllable.
     */
    public Tone getTone() {
        return m_tone;
    }

    /**
     * Indicates whether this syllable has an (additional) "r"-final. This does not mean that
     * {@link Syllable#getFinal()} is replaced by "r", but that an "r" is added behind the normal
     * final (e.g. "wanr"). This flag is also used in the creation of the syllable "er".
     *
     * @return <tt>true</tt>, if and only if this syllable has an "r"-final.
     */
    public boolean hasRFinal() {
        return m_rFinal;
    }

    /**
     * Indicates whether the first character in this syllable is capitalized.
     *
     * @return <tt>true</tt>, if and only if the first character in this syllable is capitalized.
     */
    public boolean isCapitalized() {
        return m_capitalized;
    }

    /**
     * Indicates whether this syllable has an unambiguous start. This means that it does not start
     * with a vowel, which could interfere with the coda of the previous syllable.
     *
     * @return <tt>true</tt>, if and only if this syllable has an unambiguous start.
     */
    public boolean hasUnambiguousStart() {
        return m_initial != Initial.EMPTY || m_final.hasUnambiguousIsolatedStart();
    }

    /**
     * Indicates whether this syllable has a coda (such as "n", "ng", "r", "nr", or "ngr").
     *
     * @return <tt>true</tt>, if and only if this syllable has a coda.
     */
    public boolean hasCoda() {
        return m_final.hasCoda() || m_rFinal;
    }

    /**
     * Converts this syllable into a short (16-bit integer), which can be reconstructed without
     * loss using {@link Syllable#fromShort(short)}.
     *
     * @return A 16-bit representation containing all the information of this syllable.
     */
    public short toShort() {
        short result = (short)m_initial.getIndex();
        result |= (short)m_final.getIndex() << FINAL_SHIFT;
        result |= (short)m_tone.getIndex() << TONE_SHIFT;
        if (m_rFinal) result |= R_FINAL_MASK;
        if (m_capitalized) result |= CAPITAL_MASK;
        return result;
    }

    private static final Syllable[] BUFFER = new Syllable[65536];

    /**
     * Constructs a syllable from a short (16-bit integer). This is the inverse function of
     * {@link Syllable#toShort()}.
     *
     * @param representation The 16-bit representation of the syllable to construct.
     * @return A new syllable constructed from the 16-bit representation.
     */
    public static Syllable fromShort(short representation) {
        final int index = (int)representation & 0xffff;
        if (BUFFER[index] != null) return BUFFER[index];

        final Initial initial = Initial.fromIndex(representation & INITIAL_MASK);
        final Final final_ = Final.fromIndex((representation & FINAL_MASK) >> FINAL_SHIFT);
        final Tone tone = Tone.fromIndex((representation & TONE_MASK) >> TONE_SHIFT);
        final boolean rFinal = (representation & R_FINAL_MASK) != 0;
        final boolean capitalized = (representation & CAPITAL_MASK) != 0;
        final Syllable result = new Syllable(initial, final_, tone, rFinal, capitalized);
        BUFFER[index] = result;
        return result;
    }

    private int getDiacriticIndex(String spelling) {
        // 1st option: either a or e take it. There is no syllable with a and e or o and e.
        int result = Math.max(spelling.indexOf('a'), spelling.indexOf('e'));
        if (result >= 0) return result;

        // 2nd option: the o in "ou" takes it.
        result = spelling.indexOf("ou");
        if (result >= 0) return result;

        // 3rd option: the last vowel in the syllable takes it.
        for (char v : PinyinParser.VOWELS) {
            final int vIndex = spelling.indexOf(v);
            if (vIndex > result)
                result = vIndex;
        }

        return result;
    }

    @Override
    @SuppressWarnings("unused")
    public boolean equals(Object other) {
        if (!(other instanceof Syllable)) return false;
        final Syllable otherSyllable = (Syllable)other;
        if (m_initial != otherSyllable.getInitial()) return false;
        if (m_final != otherSyllable.getFinal()) return false;
        if (m_tone != otherSyllable.getTone()) return false;
        if (m_rFinal ^ otherSyllable.hasRFinal()) return false;
        return !PinyinString.CASE_SENSITIVE || (m_capitalized == otherSyllable.isCapitalized());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder()
            .append(m_initial.getSpelling())
            .append(m_final.getSpelling(m_initial));
        if (m_rFinal) sb.append("r");
        sb.insert(getDiacriticIndex(sb.toString()) + 1, m_tone.getMark());
        final String result = sb.toString();
        if (m_capitalized)
            return Character.toUpperCase(result.charAt(0)) + result.substring(1);
        return result;
    }
}
