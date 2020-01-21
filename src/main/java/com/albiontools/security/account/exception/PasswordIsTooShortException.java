package com.albiontools.security.account.exception;

public class PasswordIsTooShortException extends Exception {
	public PasswordIsTooShortException(final String message) {
        super(message);
    }
}
