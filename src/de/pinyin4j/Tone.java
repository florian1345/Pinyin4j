package de.pinyin4j;

/**
 * An enumeration of the four chinese tones plus the neutral tone.
 */
public enum Tone {

    /**
     * The neutral tone. An example is \u7684.
     */
    NEUTRAL(0, ""),

    /**
     * The first/high tone (/\u02e5/), which is marked like thi\u0304s. An example is \u9ad8.
     */
    HIGH(1, "\u0304"),

    /**
     * The second/rising tone (/\u02e7\u02e5/), which is marked like thi\u0301s. An example is
     * \u4eba.
     */
    RISING(2, "\u0301"),

    /**
     * The third/low/dipping tone (/\u02e8\u02e9/ or /\u02e8\u02e9\u02e6/, depending on the
     * situation), which is marked like thi\u030cs. An example is \u5199.
     */
    LOW(3, "\u030C"),

    /**
     * The fourth/falling tone (/\u02e5\u02e9/), which is marked like thi\u0300s. An example
     * is \u53bb.
     */
    FALLING(4, "\u0300");

    private static Tone[] tones = new Tone[5];

    private int m_index;
    private String m_mark;

    Tone(int index, String mark) {
        m_index = index;
        m_mark = mark;
    }

    /**
     * Gets a unique index of this tone. This is also the ordinal associated with this tone in
     * Chinese linguistics (e.g. the first tone is {@link Tone#HIGH} and has index 1).
     * {@link Tone#NEUTRAL} has index 0.
     *
     * @return A unique index of this tone.
     */
    public int getIndex() {
        return m_index;
    }

    /**
     * Gets the tone mark of this tone that is placed above a vowel. For {@link Tone#NEUTRAL},
     * this will be an empty string.
     *
     * @return The tone mark of this tone.
     */
    public String getMark() {
        return m_mark;
    }

    /**
     * Looks up the tone associated with the given index. In particular, finds a tone such that
     * {@link Tone#getIndex()} return <tt>index</tt>.
     *
     * @param index The index to look for.
     * @return The tone associated with the given index.
     * @throws ArrayIndexOutOfBoundsException If there is no tone with the given index.
     */
    public static Tone fromIndex(int index) {
        return tones[index];
    }

    static {
        for (Tone tone : values()) {
            tones[tone.getIndex()] = tone;
        }
    }
}
