package com.nuga.curation.service.bid;

import java.util.List;

import com.nuga.curation.domain.article.dto.ArticleDto.ArticleResponse;
import com.nuga.curation.domain.article.dto.BidRequest;
import com.nuga.curation.exception.BidAccessException;
import com.nuga.curation.exception.EndTimeException;
import com.nuga.curation.exception.NotEnoughCoinException;
import com.nuga.curation.exception.SameTimeAccessException;
public interface BidService {
    public Long bidTry(Long userId, BidRequest request) throws NotEnoughCoinException, SameTimeAccessException, EndTimeException;
    public void bidUpdate();
    public List<ArticleResponse> bidTryArticles(Long userId);
    public void receiveProduct(Long userId,Long articleId) throws BidAccessException;
}
