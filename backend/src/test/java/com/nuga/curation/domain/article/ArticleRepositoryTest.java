package com.nuga.curation.domain.article;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.article.entity.Tag;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.article.TagDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.domain.user.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleRepositoryTest {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private TagDao tagDao;

    @Test
    public void article_save_and_modify_test(){
        User user = userDao.findById(1L).get();
        Article article = new Article();
        article.setMinUnit(300L);
        article.setPrice(10000L);
        article.setState(3);
        article.setTitle("두 번째 글");
        article.setSeller(user);
        Long id = articleDao.save(article).getArticleId();
        Article savedArticle = articleDao.findById(id).get();
        System.out.println(savedArticle.getSeller().getUser_id());
        //수정
        User modifyUser = userDao.findById(2L).get();
        savedArticle.setSeller(modifyUser);
        Long mId = articleDao.save(savedArticle).getArticleId();
        Article modifyArticle = articleDao.findById(mId).get();
        System.out.println("수정된 판매자 정보 : " + modifyArticle.getSeller().getUser_id());
        org.assertj.core.api.Assertions.assertThat(modifyArticle.getSeller()).isNotEqualTo(savedArticle.getSeller());
    }

    @Test
    public void add_tag_test(){
        Tag tag = new Tag();
        tag.setTagName("첫번째태그이름");
        tagDao.save(tag);
    }

    @Test
    public void search_Article_Test(){
        List<Article> list = articleDao.findSearchArticle("우");
        System.out.println("리스트사이즈 : " + list.size());
        for(Article a : list){
            System.out.println(a.getArticleId() + " : " + a.getTitle());
        }
    }

}
