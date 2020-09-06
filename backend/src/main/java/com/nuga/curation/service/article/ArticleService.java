package com.nuga.curation.service.article;

import com.nuga.curation.domain.article.dto.ArticleDto;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.util.List;

public interface ArticleService {
    public Long writeArticleReceiver(Long userId, ArticleDto.ArticleWriteRequest request) throws ParseException;
    public List<ArticleDto.ArticleResponse> articleList();
    public List<ArticleDto.ArticleResponse> articleList(Long userId,String categoryName); //회원특화 글 리스트
    public ArticleDto.ArticleResponse articleDetail(Long articleId,Long userId);
    public List<ArticleDto.ArticleResponse> myArticles(String nickName);
    //1.키워드가 포함된 유저정보(닉네임,이메일)과 팔로우정보가포함된 리스트와 2. 키워드가 포함된 피드를 가진 article 리스트
    public ArticleDto.SearchResponse searchArticle(String keyword,Long userId);
    public boolean deleteArticle(Long articleId);
    public List<ArticleDto.ArticleResponse> categoryArticles(String categoryName);
}
