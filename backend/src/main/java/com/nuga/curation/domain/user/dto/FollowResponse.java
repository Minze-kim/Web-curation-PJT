package com.nuga.curation.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FollowResponse {
    private String email;
    private String nickName;
    private Long coin;
    private boolean followed;
}
