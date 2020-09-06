package com.nuga.curation.domain.wish;


import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.repository.wish.WishDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WishRepositoryTest {

    @Autowired
    private WishDao wishDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private ArticleDao articleDao;

    @Test
    public void wish_test(){
        List<User> users = userDao.findAll();
        List<Article> articles =  articleDao.findAll();

        User firstUser = users.get(0);
        Article firstArticle = articles.get(0);
        //firstUser가 firstArticle을 찜하는 행위
        Wish saveWish = Wish.builder().user(firstUser).article(firstArticle).build();
        Long savedWishId = wishDao.save(saveWish).getWishId();

        List<Wish> wishList = wishDao.findAll();
        Wish lastSavedWish = wishList.get(wishList.size()-1);

        org.assertj.core.api.Assertions.assertThat(lastSavedWish.getUser().getUser_id()).isEqualTo(firstUser.getUser_id());
        org.assertj.core.api.Assertions.assertThat(lastSavedWish.getArticle().getArticleId()).isEqualTo(firstArticle.getArticleId());

    }
}
