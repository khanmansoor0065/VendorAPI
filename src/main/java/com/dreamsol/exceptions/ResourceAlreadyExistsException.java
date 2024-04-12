package com.dreamsol.exceptions;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResourceAlreadyExistsException extends RuntimeException
{
	String resourceName;

public ResourceAlreadyExistsException(String resourceName) {
		super(String.format("%s already exist!!", resourceName));
		this.resourceName = resourceName;
	}
	

}
