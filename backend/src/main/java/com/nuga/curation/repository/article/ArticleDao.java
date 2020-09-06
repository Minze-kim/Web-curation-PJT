package com.nuga.curation.repository.article;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleDao extends JpaRepository<Article,Long> {
    //알람 정보 저장을 위해서 스케줄러가 mintime 간격으로 실행되어 1분 사이에 마감된 아티클 리스트를 뽑아서 size가 1이상일경우 알람에저장
    List<Article> findArticlesByExpireDateGreaterThanAndExpireDateLessThan(LocalDateTime mintime, LocalDateTime currentTime);
    //User가 판매중인 상품중에 expireDate 이전의 글만 뽑기
    List<Article> findArticlesBySellerAndExpireDateLessThanOrderByExpireDate(User user, LocalDateTime now);
    //전체글을 최신 순으로
    List<Article> findAllByOrderByCreatedDateDesc();

    //유저 판매마감상품 리스트
    List<Article> findArticlesBySellerOrderByExpireDateDesc(User seller);
    //유저 판매history 리스트
    List<Article> findArticlesBySellerOrderByExpireDate(User seller);
    //글에 등록된 태그중에 {keyword}가 포함된 아티클리스트
    @Query("select a from Article a left join ArticleTag art on a = art.article left join Tag t on art.tag = t WHERE t.tagName LIKE CONCAT('%',:keyword,'%') OR a.title LIKE CONCAT('%',:keyword,'%') OR a.description LIKE CONCAT('%',:keyword,'%') group by a.articleId")
    List<Article> findSearchArticle(@Param("keyword") String keyword);

    //카테고리별 article 리스트
    @Query("select a from Article a join Category c on a.category = c where c.categoryName = :categoryName")
    List<Article> findArticlesByCategory(@Param("categoryName") String categoryName);



}
