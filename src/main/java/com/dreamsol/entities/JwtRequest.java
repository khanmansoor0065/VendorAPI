package com.dreamsol.entities;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtRequest
{
    private String username;
    private String password;
}
