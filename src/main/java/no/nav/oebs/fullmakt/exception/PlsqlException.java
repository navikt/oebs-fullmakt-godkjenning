package no.nav.oebs.fullmakt.exception;


public abstract class PlsqlException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	protected PlsqlException(String message) {
		super(message);
	}
}
