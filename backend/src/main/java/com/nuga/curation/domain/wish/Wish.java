package com.nuga.curation.domain.wish;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"user_id","article_id"}))
public class Wish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wishId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="article_id")
    private Article article;

    @Builder
    public Wish(User user,Article article){
        this.user = user;
        this.article = article;
    }
}
