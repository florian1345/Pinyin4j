package de.pinyin4j;

/**
 * An {@link Exception} thrown whenever something went wrong during parsing.
 */
public final class ParseException extends Exception {

	private static final long serialVersionUID = 4377114932493169493L;

	/**
     * Creates a new parse exception from the message.
     *
     * @param message A {@link String} containing the exception message.
     */
    public ParseException(String message) {
        super(message);
    }
}
