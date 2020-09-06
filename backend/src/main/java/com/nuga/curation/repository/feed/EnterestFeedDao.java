package com.nuga.curation.repository.feed;

import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.enterestfeed.EnterestFeed;
import com.nuga.curation.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;
public interface EnterestFeedDao extends JpaRepository<EnterestFeed,Long> {
    //유저가 등록해놓은 피드네임 리스트
    @Query("select f.feedName from Feed f join EnterestFeed ef on f = ef.feed where ef.user = :user")
    List<String> findEnterestFeedsByUser(@Param("user") User user);

    //유저가 등록해놓은 피드리스트 모두 제거
    void deleteByUser(User user);

    //tagName에 해당하는 관심피드를 등록한 모든 유저에게 알람을 저장
    @Query("select u from User u join EnterestFeed ef on u = ef.user join Feed f on ef.feed = f where f.feedName = :feedName")
    List<User> findUsersToFeedName(@Param("feedName") String feedName);
}
