package com.nuga.curation.domain.enterestfeed;

import com.nuga.curation.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@Entity
public class EnterestFeed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long enterestfeedId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "feed_id")
    private Feed feed;

    @Builder
    public EnterestFeed(User user, Feed feed){
        this.user = user;
        this.feed = feed;
    }
}
