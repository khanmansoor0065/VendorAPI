package com.dreamsol.exceptions;

public class ResourceAlreadyExistsException extends RuntimeException
{
	String resourceName;

	public ResourceAlreadyExistsException(String resourceName) {
		super(String.format("%s already exist!!", resourceName));
		this.resourceName = resourceName;
	}
	

}
