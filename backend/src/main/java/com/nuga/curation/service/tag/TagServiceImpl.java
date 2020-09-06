package com.nuga.curation.service.tag;

import com.nuga.curation.domain.enterestfeed.EnterestFeed;
import com.nuga.curation.domain.enterestfeed.Feed;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.repository.article.ArticleTagDao;
import com.nuga.curation.repository.article.TagDao;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.article.entity.ArticleTag;
import com.nuga.curation.domain.article.entity.Tag;
import com.nuga.curation.domain.article.dto.TagRequest;
import com.nuga.curation.repository.feed.EnterestFeedDao;
import com.nuga.curation.repository.feed.FeedDao;
import com.nuga.curation.service.bid.BidService;
import com.nuga.curation.service.bid.BidServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;
    private final ArticleTagDao articleTagDao;
    private final BidServiceImpl bidService;
    private final FeedDao feedDao;
    private final EnterestFeedDao enterestFeedDao;
    private final Long HASH_KEY = 1000000L;
    //태그리스트의 name을 확인하며 Tag 테이블에 존재하지 않는 name을 추가한다.
    @Override
    @Transactional
    public void addTagList(List<TagRequest> tagList) {
        for(TagRequest tag : tagList){
            if(tagDao.findByTagName(tag.getTagName())!=null) continue;
            tagDao.save(tag.toEntity());
        }
    }

    @Override
    @Transactional
    public void addTagToArticle(Article request, List<TagRequest> tagList) {
        for(int i=0;i<tagList.size();i++){
            Tag existedTag = tagDao.findByTagName(tagList.get(i).toEntity().getTagName());
            articleTagDao.save(ArticleTag.builder()
                    .article(request)
                    .tag(existedTag)
                    .build());
        }
        //태그이름에 해당하는 것을 관심피드로 등록한 사용자에게 보낼 알람 저장
        sendAlarmRegisteredWishFeed(request,tagList);
    }

    //알람 저장하는 부분 (state = 6) ==> 관심피드글이 등록 됏을 때 알람 저장
    @Transactional
    public void sendAlarmRegisteredWishFeed(Article article,List<TagRequest> tagList){
        HashSet<Long> hashSet = new HashSet<Long>();
        for(TagRequest tag:tagList){
            String tagName = tag.getTagName();
            Feed feedInfo = feedDao.findFeedByFeedName(tagName);
            if(feedInfo==null)continue;
            List<User> relationedUserList = enterestFeedDao.findUsersToFeedName(tagName);
            for(User u : relationedUserList){
                if(hashSet.contains(u.getUser_id())){
                    continue;
                }
                hashSet.add(u.getUser_id());
                bidService.alarmSave(article,u,6);
            }
        }
    }
}
