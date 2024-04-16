package com.dreamsol.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class RefreshTokenRequest
{
    private  String refreshToken;
}
