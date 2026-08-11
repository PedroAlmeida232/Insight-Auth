package com.example.Insight_Auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDto {
    private String token;
    private String type;

    public TokenResponseDto(String token){
        this.token = token;
        this.type = "Bearer";
    }

}
