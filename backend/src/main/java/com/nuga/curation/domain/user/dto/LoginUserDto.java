package com.nuga.curation.domain.user.dto;

import com.nuga.curation.domain.article.dto.ArticleDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class LoginUserDto {


    @Data
    public static class LoginUser{
        private String email;
        private String nickName;
        private long coin;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    public static class LoginAlarm{
        private Long alarmId;
        private Long articleId;
        private LocalDateTime createDate;
        private boolean state;
    }

    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    public static class LoginUserResponse{
        LoginUserDto.LoginUser loginUser;
        Boolean isHaveAlarm;
        //List<LoginAlarm> loginAlarm;
    }
    //프로필 화면에서 유저판매 상품 제외하고 위에 부분 데이터
    @Getter @Setter
    @AllArgsConstructor @NoArgsConstructor
    public static class UserProfile{
        private LoginUserDto.LoginUser loginUser;
        private int sellingCount; //내가 판매중인 상품 수
        private int followerCount;
        private int followingCount;
        private int totalSellCount;
        private boolean followed;
        public void notFollow(){
            this.followed = false;
        }
        public void follow(){
            this.followed = true;
        }
    }

    //유저프로필 클릭 시 화면 전체에 뿌려줄 데이터
//    @Getter @Setter
//    @AllArgsConstructor @NoArgsConstructor
//    public static class UserProfileWithArticle{
//        private LoginUserDto.UserProfile userProfile;
//        private List<ArticleDto.ArticleResponse> userArticle;
//    }
}
