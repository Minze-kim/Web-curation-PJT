package com.nuga.curation.domain.user.dto;

import com.nuga.curation.domain.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;




@Valid
@ToString
public class SignupRequest {
    @ApiModelProperty(required = true)
    @NotNull
    String email;
    @ApiModelProperty(required = true)
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d$@$!%*#?&]{8,}$")
    String password;
    @ApiModelProperty(required = true)
    @NotNull
    String nickname;

    @Builder
    public SignupRequest(String email,String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User toEntity(){
        return User.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .coin(0L)
                .build();
    }

    public String getEmail() {
        return email;
    }


    public String getPassword() {
        return password;
    }


    public String getNickname() {
        return nickname;
    }

}
