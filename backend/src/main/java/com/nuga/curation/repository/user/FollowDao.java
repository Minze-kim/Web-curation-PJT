package com.nuga.curation.repository.user;

import com.nuga.curation.domain.follow.Follow;
import com.nuga.curation.domain.follow.FollowInfo;
import com.nuga.curation.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.*;
public interface FollowDao extends JpaRepository<Follow, FollowInfo> {
    List<Follow> getFollowsByFollowInfo_User(User user);
    List<Follow> getFollowsByFollowInfo_Target(User target);
    Follow getFollowByFollowInfo_UserAndFollowInfo_Target(User user,User target);
    //둘 사이가 팔로우 관계인지 아닌지
//    @Query("select f from Follow f where (f.followInfo.user = :user AND f.followInfo.target = :target) OR (f.followInfo.target = :user AND f.followInfo.user = :target)")
//    Follow getFollowByFollowInfo(User user, User target);
}
