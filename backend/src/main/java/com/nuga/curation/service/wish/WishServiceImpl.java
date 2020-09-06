package com.nuga.curation.service.wish;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.category.Category;
import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.domain.wish.Wish;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.repository.wish.WishDao;
import com.nuga.curation.service.article.ArticleServiceImpl;
import com.nuga.curation.repository.article.ImageDao;
import org.modelmapper.ModelMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishServiceImpl implements WishService{

    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final WishDao wishDao;
    private final ArticleServiceImpl ArticleServiceImpl;
    private final ModelMapper modelMapper;
    private final ImageDao imageDao;


    @Override
    @Transactional
    public String wishToArticle(Long userId, Long articleId) {
        StringBuilder message = new StringBuilder();
        User user = userDao.findById(userId).get();
        Article article = articleDao.findById(articleId).get();
        Wish wishInfo = wishDao.getWishByUserAndArticle(user,article);
        if(article.getSeller()==user) {
            throw new UnauthorizedException();
        }
        if(wishInfo==null){
            wishInfo = Wish.builder()
                    .user(user)
                    .article(article)
                    .build();
            article.wishUpdate(1);
            wishDao.save(wishInfo);
            message.append("해당상품을 찜했습니다");
        }else{
            article.wishUpdate(-1);
            wishDao.delete(wishInfo);
            message.append("해당상품을 찜목록에서 삭제했습니다");
        }
        return message.toString();
    }

    @Override
    public List<ArticleDto.ArticleResponse> wishList(Long userId) {
        List<Article> wishInfo = wishDao.getArticleByUserId(userId);
        return wishInfo.stream()
                .map(this::articleMapping)
                .collect(Collectors.toList());
        /*List<ArticleDto.ArticleResponse> wishList = new ArrayList<ArticleDto.ArticleResponse>();
        for(Article article : wishInfo){
            ArticleDto.ArticleResponse wish = new ArticleDto.ArticleResponse(article.getArticleId(),
                    article.getSeller().getUser_id(), article.getSeller().getNickName(), article.getBuyer().getNickName(),
                    article.getPrice(), article.getImmediatePrice(), article.getMinUnit(), article.getTitle(), article.getDescription(),
                    article.getPurchaseDate(), article.getExpireDate(), article.getCreatedDate(), article.getState(), article.getExpireDate());
            wishList.add(wish);
        }
        return wishList;*/
    }
    
    private ArticleDto.ArticleResponse articleMapping(Article articleInfo) {
        ArticleDto.ArticleResponse response = modelMapper.map(articleInfo,ArticleDto.ArticleResponse.class);
        response.setSellerNickname(articleInfo.getSeller().getNickName());
        response.setSellerId(articleInfo.getSeller().getUser_id());

        response.setTags(ArticleServiceImpl.tagsToArticle(articleInfo));
        response.setImages(ArticleServiceImpl.imageToString(imageDao.findImageByArticle(articleInfo)));

        User buyer = articleInfo.getBuyer();
        Category category= articleInfo.getCategory();

        if(buyer!=null) response.setBuyerNickname(buyer.getNickName());
        if(category!=null) response.setCategoryName(category.getCategoryName());
        return response;
    }
}
