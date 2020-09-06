package com.nuga.curation.repository.article;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.*;

public interface ImageDao extends JpaRepository<Image,Long> {
    List<Image> findImageByArticle(Article article);
}
