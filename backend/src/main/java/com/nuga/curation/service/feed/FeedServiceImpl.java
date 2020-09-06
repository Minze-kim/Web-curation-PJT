package com.nuga.curation.service.feed;

import com.nuga.curation.domain.enterestfeed.EnterestFeed;
import com.nuga.curation.domain.enterestfeed.Feed;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.repository.feed.EnterestFeedDao;
import com.nuga.curation.repository.feed.FeedDao;
import com.nuga.curation.repository.user.UserDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class FeedServiceImpl implements FeedService{

    private final UserDao userDao;
    private final EnterestFeedDao enterestFeedDao;
    private final FeedDao feedDao;
    //유저가 가진 모든 피드리스트를 보여준다
    @Override
    public List<String> feedList(Long userId) {
        User user = userDao.findById(userId).get();
        List<String> feedList = enterestFeedDao.findEnterestFeedsByUser(user);
        return feedList;
    }

    @Override
    @Transactional
    public void addFeed(Long userId, List<String> feedList) {
        User user = userDao.findById(userId).get();
        addFeedWithoutDupl(feedList);
        enterestFeedDao.deleteByUser(user); //유저의 피드리스트 제거
        for(String x : feedList){
            Feed findFeed = feedDao.findFeedByFeedName(x);
            enterestFeedDao.save(EnterestFeed.builder().user(user).feed(findFeed).build());
        }
    }

    //피드이름 정보 테이블(중복 없음)
    public void addFeedWithoutDupl(List<String> feedList){
        for(String x : feedList){
            Feed findFeed = feedDao.findFeedByFeedName(x);
            if(findFeed!=null) continue;
            feedDao.save(Feed.builder().feedName(x).build());
        }
    }

}
