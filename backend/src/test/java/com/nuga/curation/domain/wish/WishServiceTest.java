package com.nuga.curation.domain.wish;

import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.service.wish.WishService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class WishServiceTest {
    @Autowired
    private WishService wishService;

    @Test
    public void wish_list_to_user_test(){
        Long userId= 1L;

        List<ArticleDto.WishResponseToUser> wishList =  wishService.wishList(userId);
        for(ArticleDto.WishResponseToUser wishinfo : wishList){
            System.out.println("글 번호 : " + wishinfo.getArticleId());
        }

    }
}
