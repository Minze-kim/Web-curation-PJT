
package com.nuga.curation.domain.user.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.wish.Wish;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicUpdate
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long user_id;

    @JsonIgnore
    private String password;
    private String email;
    private String nickName;

    @ColumnDefault("0") //default 0
    private long coin;

    @ManyToMany
    @JoinTable(name="FOLLOW",joinColumns = @JoinColumn(name="following_user"),
            inverseJoinColumns = @JoinColumn(name="followed_user"))
    private Set<User> followers = new HashSet<User>();

    @ManyToMany
    @JoinTable(name="FOLLOW",joinColumns = @JoinColumn(name="followed_user"),
            inverseJoinColumns = @JoinColumn(name="following_user"))
    private Set<User> following = new HashSet<User>();


    @OneToMany(mappedBy = "seller",cascade = CascadeType.ALL)
    private List<Article> sellList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alarm> alarmList;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Wish> wishList;

    @CreatedDate
    @Column(columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)", insertable = false, updatable = false)
    private LocalDateTime createDate;

    @Builder
    public User(String password, String email, String nickname, Set<User> followers, Set<User> following,Long coin) {
        this.password = password;
        this.email = email;
        this.nickName = nickname;
        this.followers = followers;
        this.following = following;
        this.coin = coin;
    }

    public User DateSetting(LocalDateTime dateTime) {
        this.createDate = dateTime;
        return this;
    }

    public void addFollow(User targetUser){
        this.following.add(targetUser);
        targetUser.followers.add(this);
    }

    public void deleteFollow(User targetUser){
        System.out.println("domain함수");
        this.following.remove(targetUser);
        System.out.println("user->target 언팔성공");
        targetUser.followers.remove(this);
        System.out.println("target -> user 언팔성공 domain함수 끝");
    }

    public void addCoin(Long pay){
        this.coin += pay;
    }
}
