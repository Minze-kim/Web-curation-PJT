package com.nuga.curation.domain.article;

import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.domain.article.dto.ArticleDto.ArticleWriteRequest;
import com.nuga.curation.domain.article.dto.BidTryResponse;
import com.nuga.curation.domain.article.dto.TagRequest;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.article.entity.ArticleTag;
import com.nuga.curation.domain.article.entity.Tag;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.article.ArticleTagDao;
import com.nuga.curation.repository.article.TagDao;
import com.nuga.curation.service.article.ArticleService;
import com.nuga.curation.service.bid.BidService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ArticleServiceTest {
    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private TagDao tagDao;

    @Autowired
    private ArticleTagDao articleTagDao;
    @Autowired
    private ArticleService articleService;
    @Autowired
    private BidService bidService;
    @Test //******중요******
    @Transactional
    public void write_article_with_taglist_test() throws ParseException {
        Long writer = 1L;
        String description = "두번째글테스트입니다";
        ArticleDto.ArticleRequest articleRequest = ArticleDto.ArticleRequest.builder()
                .expireDate("1일후")
                .description(description)
                .immediatePrice(50000L)
                .minUnit(1000L)
                .price(30000L)
                .purchaseDate("2015-06-15")
                .state(5)
                .title("2번째글")
                .build();
        List<TagRequest> tagRequests = new ArrayList<TagRequest>();
        tagRequests.add(new TagRequest("컴퓨터"));
        tagRequests.add(new TagRequest("가전제품"));
        tagRequests.add(new TagRequest("한도현천재"));
        tagRequests.add(new TagRequest("슬리퍼"));
        Long id = articleService.writeArticleReceiver(1L,new ArticleWriteRequest(articleRequest,tagRequests,"",null));

        Article savedArticle = articleDao.findById(id).get();
        org.assertj.core.api.Assertions.assertThat(savedArticle.getDescription()).isEqualTo(description);
        List<ArticleTag> articleTagInfo = articleTagDao.getArticleTagsByArticle(savedArticle);
        for(TagRequest tag : tagRequests){
            boolean suc = false;
            String tagName = tag.getTagName();
            for(ArticleTag articleTag : articleTagInfo){
                if(articleTag.getTag().getTagName().equals(tagName)) suc = true;
            }
            org.assertj.core.api.Assertions.assertThat(suc).isEqualTo(true);
        }
        List<Tag> tagAllList = tagDao.findAll();
    }

    @Test
    @Transactional
    public void alarm_test(){
        List<BidTryResponse> list = bidService.bidTryArticles(1L);
        for(BidTryResponse b: list){
            System.out.println(b.getBuyer() + "에게 갈"+b.getArticleId()+ "번 글 알람 게시글 : " + b.getTitle() +"/ 상태 : " + b.getState());
        }
    }
}
