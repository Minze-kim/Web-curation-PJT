package com.nuga.curation.domain.image;

import com.nuga.curation.domain.article.entity.Article;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @NotNull
    @Column(columnDefinition = "LONGTEXT")
    private String urlPath;

    public void addImageToArticle(Article article,String fileName){
        this.article = article;
        this.urlPath = fileName;
    }

    @Builder
    public Image(Article article, String urlPath){
        this.article = article;
        this.urlPath = urlPath;
    }
}
