package com.nuga.curation.domain.alarm;

import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.domain.article.dto.TagRequest;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.repository.alarm.AlarmDao;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.service.article.ArticleService;
import com.nuga.curation.service.bid.BidService;
import com.nuga.curation.service.mail.MailService;
import com.nuga.curation.service.user.UserService;
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
public class AlarmServiceTest {
    @Autowired
    UserDao userDao;
    @Autowired
    UserService userService;
    @Autowired
    MailService mailService;
    @Autowired
    ArticleService articleService;
    @Autowired
    AlarmDao alarmDao;
    @Autowired
    ArticleDao articleDao;
    @Autowired
    BidService bidService;
    @Test
    @Transactional
    public void alarm_not_update_test() throws ParseException {
        Long writer = 1L;
        String description = "세번째글테스트입니다";
        ArticleDto.ArticleRequest articleRequest = ArticleDto.ArticleRequest.builder()
                .expireDate("1일후")
                .description(description)
                .immediatePrice(50000L)
                .minUnit(1000L)
                .price(30000L)
                .purchaseDate("2017-06-13")
                .state(5)
                .title("3번째글")
                .build();
        List<TagRequest> tagRequests = new ArrayList<TagRequest>();
        tagRequests.add(new TagRequest("컴퓨터"));
        tagRequests.add(new TagRequest("가전제품"));
        tagRequests.add(new TagRequest("한도현천재"));
        tagRequests.add(new TagRequest("슬리퍼"));
        Long id = articleService.writeArticleReceiver(1L,new ArticleDto.ArticleWriteRequest(articleRequest,tagRequests,"",null));

        Article article = articleDao.findById(id).get();
        article.setBuyer(userDao.findById(2L).get());
        bidService.bidUpdate();
        //알람 업데이트 된 상태임
        List<Alarm> alarms = alarmDao.findAll();
        for(Alarm a : alarms){
            System.out.println(a.getUser().getEmail() + "가  제목 : " +a.getArticle().getTitle() + "인 알람을 가지고 있습니다");
        }
        System.out.println("알람개수" + alarms.size());
        Article first = alarms.get(0).getArticle();
        System.out.println("first 아티클 수정 전 : " + first.getArticleId() + "/" + first.getTitle());
        first.setTitle("제목을 수정했습니다!!");
        Article second = alarms.get(1).getArticle();
        System.out.println("first 아티클 수정 후 : " + first.getArticleId() + "/" + first.getTitle() + " second : " + second.getArticleId() + "/" + second.getTitle());
    }
}
