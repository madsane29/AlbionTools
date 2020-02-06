package com.albiontools.security.account.exception;

public class NonExistentEmailException extends Exception {

	public NonExistentEmailException(final String message) {
        super(message);
    }

}