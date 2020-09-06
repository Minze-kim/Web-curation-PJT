package com.nuga.curation.repository.wish;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.domain.wish.Wish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
public interface WishDao extends JpaRepository<Wish,Long> {
    Wish getWishByUserAndArticle(User user, Article article);
    List<Wish> getWishesByUser(User user);
    @Query("select a from Article a where a.articleId in (select w.article from Wish w where w.user.user_id = :userId)")
    List<Article> getArticleByUserId(@Param("userId") Long userId);
}
