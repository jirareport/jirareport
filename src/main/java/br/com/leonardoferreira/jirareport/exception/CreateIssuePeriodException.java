package br.com.leonardoferreira.jirareport.exception;

/**
 * @author lferreira
 * @since 11/23/17 10:44 AM
 */
public class CreateIssuePeriodException extends Exception {
    private static final long serialVersionUID = -8455210961219493250L;

    public CreateIssuePeriodException(final String message) {
        super(message);
    }

    public CreateIssuePeriodException(final String message, final Throwable t) {
        super(message, t);
    }

}
