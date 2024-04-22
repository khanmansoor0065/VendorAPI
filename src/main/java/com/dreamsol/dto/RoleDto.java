package com.dreamsol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoleDto
{
    @NotEmpty(message = "role name must not be empty")
    @Pattern(regexp = "^[a-zA-Z]{3,}(\\s[a-zA-z]{3,})*$")
    @Size(min = 3, max = 20, message = "role name must be 3 to 20 characters long")
    private String role;

    @Size(min = 1, message = "endpoints must not be empty")
    private List<String> endPoints;
}
