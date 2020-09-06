package com.nuga.curation.domain.user;


import com.nuga.curation.domain.follow.Follow;
import com.nuga.curation.domain.follow.FollowInfo;
import com.nuga.curation.domain.user.dto.FollowResponse;
import com.nuga.curation.domain.user.dto.SignupRequest;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.repository.user.FollowDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FollowRepositoryTest {
    @Autowired
    EntityManagerFactory entityManagerFactory;

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    FollowDao followDao;

    @Test
    @Transactional
    public void follow_Test() throws Exception {
        String email = "lando94@naver.com";
        String nickname = "hando2";
        String password = "ssafy123!23";

        String email2 = "ssafy@naver.com";
        String nickname2 = "ssafy";
        String password2 = "ssafy123!23";
        SignupRequest signUser = SignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
        SignupRequest signUser2 = SignupRequest.builder()
                .email(email2)
                .nickname(nickname2)
                .password(password2)
                .build();

        Long lastInsertId = userDao.save(signUser.toEntity().DateSetting(LocalDateTime.now())).getUser_id();
        Long lastInsertId2 = userDao.save(signUser2.toEntity().DateSetting(LocalDateTime.now())).getUser_id();

        userService.follow(nickname,nickname2);
    }

    @Test
    @Transactional
    public void un_Follow_Test() throws Exception {
        List<FollowResponse> followList = userService.followingUsers(userDao.findById(1L).get(),null);
        List<Follow> followInfo = followDao.getFollowsByFollowInfo_User(userDao.findById(1L).get());
        User target = followInfo.get(0).getFollowInfo().getTarget();
        System.out.println("언팔 하기 전");
        for(Follow follow : followInfo){
            System.out.println(follow.getFollowInfo().getUser().getUser_id() +"->" + follow.getFollowInfo().getTarget().getUser_id());
        }
        System.out.println("언팔 할 관계 : (1L -> " + target.getUser_id() + ")");
        userService.follow("lando94@naver.com",target.getNickName());
        System.out.println("언팔 후");
        followInfo = followDao.getFollowsByFollowInfo_User(userDao.findById(1L).get());
        for(Follow follow : followInfo){
            System.out.println(follow.getFollowInfo().getUser().getUser_id() +"->" + follow.getFollowInfo().getTarget().getUser_id());
        }
    }

    @Test
    @Transactional
    public void following_List_Test(){
        Long userId=  1L;
        System.out.println(userId);
        List<FollowResponse> followingList = userService.followingUsers(userDao.findById(userId).get(),null);
        for(FollowResponse user : followingList){
            System.out.println(user.getEmail());
        }
    }

    @Test
    @Transactional
    public void followed_List_Test(){
        List<FollowResponse> followingList = userService.followedUsers(userDao.findById(1L).get(),null);
        for(FollowResponse user : followingList){
            System.out.println(user.getEmail());
        }
    }
}
