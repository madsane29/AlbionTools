package com.albiontools.security.account.exception;

public class EmptyTokenFieldException extends NullPointerException {
	public EmptyTokenFieldException(final String message) {
        super(message);
    }
}
