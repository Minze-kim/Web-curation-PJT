package com.nuga.curation.domain.user.dto;

import lombok.*;


@NoArgsConstructor
@Getter
@Setter
public class SearchUserResponse {
    private String nickName;
    private String email;
    private Boolean followed;
    @Builder
    public SearchUserResponse(String nickName, String email,Boolean followed){
        this.email = email;
        this.nickName = nickName;
        this.followed = followed;
    }
}
