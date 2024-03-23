package com.dreamsol.exceptions;

public class EmptyVendorListException extends RuntimeException {
    String resourceName;

    public EmptyVendorListException(String resourceName) {
        super(String.format("%s", resourceName));
        this.resourceName = resourceName;
    }

}