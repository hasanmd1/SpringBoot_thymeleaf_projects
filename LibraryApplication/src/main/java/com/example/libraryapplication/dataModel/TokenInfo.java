package com.example.libraryapplication.dataModel;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenInfo {
    private String token;
    private LocalDateTime expiryTime;
}
