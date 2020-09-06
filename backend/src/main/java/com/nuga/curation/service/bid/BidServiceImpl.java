
package com.nuga.curation.service.bid;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.article.dto.ArticleDto.ArticleResponse;
import com.nuga.curation.domain.article.dto.BidRequest;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.image.Image;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.exception.BidAccessException;
import com.nuga.curation.exception.EndTimeException;
import com.nuga.curation.exception.NotEnoughCoinException;
import com.nuga.curation.exception.SameTimeAccessException;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.repository.alarm.AlarmDao;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.article.ImageDao;
import com.nuga.curation.repository.user.UserDao;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BidServiceImpl implements BidService{
    private final UserDao userDao;
    private final ArticleDao articleDao;
    private final AlarmDao alarmDao;
    private final ModelMapper modelMapper;
    private final ImageDao imageDao;
    @Override
    @Transactional
    public Long bidTry(Long userId, BidRequest request) throws NotEnoughCoinException, SameTimeAccessException, EndTimeException {
        User tryUser = userDao.findById(userId).get();
        Article tryArticle = articleDao.findById(request.getArticleId()).get();
        Long tryCoin = request.getCoin();
        if(tryUser == tryArticle.getSeller()){  //본인의 글에 입찰하는 행위 불가
            throw new UnauthorizedException();
        }
        if(tryUser.getCoin()< tryCoin){
            throw new NotEnoughCoinException("코인이 충분하지 않습니다");
        }
        if(tryArticle.getPrice() >= tryCoin || (tryArticle.getPrice().equals(tryArticle.getImmediatePrice()))){
            throw new SameTimeAccessException("다른 사람이 상품에 대한 입찰을 먼저 시도했거나 즉시 구매 했습니다!");
        }
        if(tryArticle.getExpireDate().isBefore(LocalDateTime.now())){
            throw new EndTimeException("입찰 가능 시간이 마감되었습니다.");
        }
        Long id = null;
        //즉시구매일 경우
        if(tryArticle.getImmediatePrice()<=tryCoin) {
            tryCoin = tryArticle.getImmediatePrice();
            return immediateBuy(tryUser,tryArticle,tryCoin);
        }
        //알람 저장 (본인이 입찰 최고가를 갱신하는경우에도 알림을 저장함 => 응답용 x )
        alarmSave(tryArticle,tryUser,1);
        if(tryArticle.getBuyer()!=null && tryArticle.getBuyer() != tryUser) {
            alarmSave(tryArticle, tryArticle.getBuyer(),2);
        }
        id = tryArticle.bidInfoUpdate(tryUser,tryCoin).getArticleId();  //buyer 설정, coin update
        return id;
    }

    //60분 간격 스케쥴 함수 => 알람저장
    @Override
    @Transactional
    public void bidUpdate()  {
        List<Article> updateList = articleDao.
                findArticlesByExpireDateGreaterThanAndExpireDateLessThan(LocalDateTime.now().minusMinutes(60), LocalDateTime.now());

        for(Article a : updateList){
            User buyer = a.getBuyer();
            User seller = a.getSeller();
            if(buyer!=null) { //마감된 상품에 입찰자가 있을 경우 마지막 입찰자 알람 저장
                Alarm info = alarmDao.findAlarmByUserAndArticleAndKind(buyer,a,3);
                if(info!=null) continue;
                alarmSave(a,buyer,3);
                alarmSave(a,seller,4);
                //seller에게 코인을 주어야함 ==> 언제 처리할 지 아직 정하지 않음
            }else{
                Alarm info = alarmDao.findAlarmByUserAndArticleAndKind(seller,a,5);
                if(info!=null)continue;
                alarmSave(a,seller,5);
            }
        }
    }

    public List<String> imageToString(List<Image> list){
        List<String> ret = new ArrayList<String>();
        for(Image i: list){
            ret.add(i.getUrlPath());
        }
        return ret;
    }
    
    @Override
    public List<ArticleResponse> bidTryArticles(Long userId) {
        User user = userDao.findById(userId).get();
        List<Alarm> alarms = alarmDao.findAlarmsByUserAndKindLessThanOrderByCreateDateDesc(3,user);
        HashSet<Long> hashSet = new HashSet<Long>();
        List<ArticleResponse> responses = new ArrayList<ArticleResponse>();
        for(Alarm alarm : alarms){
            Article article = alarm.getArticle();
            Long articleId =  article.getArticleId();
            if (hashSet.contains(articleId)) continue;
            hashSet.add(articleId);
            ArticleResponse dto = modelMapper.map(article, ArticleResponse.class);
            if (alarm.getKind()==1) {
                dto.setState(1);
            } else if (alarm.getKind()==2) {
                dto.setState(2);
            } else if (alarm.getKind()==3) {
                dto.setState(3);
            }
            dto.setImages(imageToString(imageDao.findImageByArticle(article)));
            responses.add(dto);
        }
        return responses;
    }

    @Override
    @Transactional
    public void receiveProduct(Long userId, Long articleId) throws BidAccessException {
        Article article = articleDao.findById(articleId).get();
        User buyer = userDao.findById(userId).get();
        User seller = article.getSeller();
        Alarm info = alarmDao.findAlarmByUserAndArticleAndKind(seller,article,7);
        Alarm info2 = alarmDao.findAlarmByUserAndArticleAndKind(buyer,article,3);
        if(info!=null){
            throw new BidAccessException("물품을 이미 수령하셨으며 해당 글은 더이상 입찰진행 불가합니다");
        }
        if(buyer!= article.getBuyer()){
            throw new BidAccessException("본인 물품이 아니면 해당 요청을 하실 수 없습니다");
        }
        if(info2 == null){
            throw new BidAccessException("아직 입찰 완료되지 않은 상품이거나 본인이 낙찰자가 아닙니다");
        }
        article.sellEnd();
        alarmSave(article,seller,7);
    }

    //알람 저장
    @Transactional
    public void alarmSave(Article article, User original,Integer kind){
        Alarm alarm = Alarm.builder()
                .article(article)
                .user(original)
                .state(false)
                .kind(kind)
                .build();
        alarm.setDateTime(LocalDateTime.now());
        alarmDao.save(alarm);
    }

    @Transactional
    public Long immediateBuy(User user, Article article, Long coin){
        User originUser = article.getBuyer();
        if(originUser!=null){
            alarmSave(article,originUser,2);
        }
        alarmSave(article,user,3);
        alarmSave(article,article.getSeller(),4);
        return article.bidInfoUpdate(user,coin).getArticleId();
    }
}
