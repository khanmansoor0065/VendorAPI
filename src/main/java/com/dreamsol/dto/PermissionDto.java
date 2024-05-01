package com.dreamsol.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PermissionDto
{
    @NotEmpty(message = "Permission must not be empty")
    @Pattern(regexp = "^[a-zA-Z]{3,}(\\s[a-zA-z]{3,})*$")
    @Size(min = 3, max = 20, message = "Permission must have 3 to 20 characters")
    private String permission;

    @Size(min = 1, message = "Endpoints must not be empty")
    private List<String> endPoints;
}
