package com.nuga.curation.domain.user.dto;


import lombok.*;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationRequest {
    
    String email;
    String password;
    String name;
    
}
