package com.optimagrowth.license.exception;

public class LicenseException extends RuntimeException {

    public LicenseException(String message) {
        super(message);
    }

    public LicenseException(Throwable cause) {
        super(cause);
    }

    public LicenseException(String message, Throwable cause) {
        super(message, cause);
    }

}
