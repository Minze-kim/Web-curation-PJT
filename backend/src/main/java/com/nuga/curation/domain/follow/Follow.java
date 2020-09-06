package com.nuga.curation.domain.follow;

import com.nuga.curation.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
public class Follow{

    @EmbeddedId
    private FollowInfo followInfo;

    //팔로우행위
    public void follow(User followedUser, User followingUser){
        this.followInfo = new FollowInfo();
        this.followInfo.setUser(followedUser);
        this.followInfo.setTarget(followingUser);
    }
}

