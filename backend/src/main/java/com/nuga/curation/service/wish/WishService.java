package com.nuga.curation.service.wish;

import com.nuga.curation.domain.article.dto.ArticleDto;
import java.util.*;
public interface WishService {
    public String wishToArticle(Long userId,Long articleId);
    public List<ArticleDto.ArticleResponse> wishList(Long userId);
}
