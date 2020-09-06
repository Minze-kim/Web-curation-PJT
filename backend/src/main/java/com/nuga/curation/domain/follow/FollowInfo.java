package com.nuga.curation.domain.follow;

import com.nuga.curation.domain.user.entity.User;
import lombok.Getter;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
@Getter
public class FollowInfo implements Serializable {

    @OneToOne
    @JoinColumn(name="followed_user" )
    private User user;

    @OneToOne
    @JoinColumn(name="following_user")
    private User target;

    public void setUser(User user) {
        this.user = user;
    }

    public void setTarget(User target) {
        this.target = target;
    }
}
