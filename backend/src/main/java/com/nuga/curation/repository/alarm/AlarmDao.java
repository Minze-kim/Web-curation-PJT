package com.nuga.curation.repository.alarm;

import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AlarmDao extends JpaRepository<Alarm,Long> {
    //유저와 관련된 알람 중 안 읽은 알람
    List<Alarm> findAlarmsByUserAndStateOrderByCreateDateDesc(User user, boolean state);

    //입찰관련 모든 알람
    @Query("select a from Alarm a where a.kind <= :kind AND a.user = :user order by a.createDate desc")
    List<Alarm> findAlarmsByUserAndKindLessThanOrderByCreateDateDesc(@Param("kind") Integer kind, @Param("user") User user);

    //유저 관련 모든 알람 정보(수정)
    @Query("select a from Alarm  a where a.user = :user AND a.kind <> :kind order by a.createDate desc")
    List<Alarm> findAlarmsByUserExceptKind(@Param("user") User user, @Param("kind") Integer kind);

    //유저별 알람종류별 search
    Alarm findAlarmByUserAndArticleAndKind(User user,Article article, Integer kind);
}