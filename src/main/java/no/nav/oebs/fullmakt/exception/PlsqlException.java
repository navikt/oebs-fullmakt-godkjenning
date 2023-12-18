package no.nav.oebs.fullmakt.exception;

import no.nav.oebs.fullmakt.config.common.logging.LoggingUtils;

public abstract class PlsqlException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public PlsqlException(String message) {
		super(message);
	}
}
