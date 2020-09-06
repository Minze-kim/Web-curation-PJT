package com.nuga.curation.repository.article;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.article.entity.ArticleTag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface ArticleTagDao extends JpaRepository<ArticleTag,Long> {
    List<ArticleTag> getArticleTagsByArticle(Article article);

}
