package com.nuga.curation.domain.article.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ArticleTagId;  //article_tag_id

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name="tag_id")
    private Tag tag;

    @Builder
    public ArticleTag(Article article,Tag tag){
        this.article = article;
        this.tag = tag;
    }
}
