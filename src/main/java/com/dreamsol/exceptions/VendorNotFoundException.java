package com.dreamsol.exceptions;

public class VendorNotFoundException extends RuntimeException {

    public VendorNotFoundException() {
        super();
    }

    public VendorNotFoundException(String message) {
        super(message);
    }

    public VendorNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
