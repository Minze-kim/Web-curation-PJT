package com.nuga.curation.service.alarm;

import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.alarm.dto.AlarmDto;
import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.repository.alarm.AlarmDao;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.user.UserDao;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlarmServiceImpl implements AlarmService {

    private final AlarmDao alarmDao;
    private final UserDao userDaoo;
    private final ModelMapper modelMapper;
    private final Long HASH_KEY = 1000000L;
    @Override
    @Transactional
    public List<AlarmDto.UserAlarm> userAlarm(Long userId) {
        User user = userDaoo.findById(userId).get();
        List<Alarm> userAlarms= alarmDao.findAlarmsByUserExceptKind(user,1);
        HashSet<Long> hashSet = new HashSet<Long>();
        List<AlarmDto.UserAlarm> ret = new ArrayList<AlarmDto.UserAlarm>();
        for(Alarm alarms : userAlarms){
            System.out.println(alarms.toString());
            Article article = alarms.getArticle();
            if(hashSet.contains(article.getArticleId())) continue;
            if(alarms.getKind()==6){
                hashSet.add(article.getArticleId()*HASH_KEY + article.getArticleId());
            }else if(alarms.getKind()==7){
                hashSet.add(article.getArticleId()*HASH_KEY*HASH_KEY + article.getArticleId());
            } else{
                hashSet.add(article.getArticleId());
            }

            ret.add(AlarmDto.UserAlarm.builder()
                    .articleId(article.getArticleId())
                    .title(article.getTitle())
                    .state(alarms.getKind())
                    .build());
        }
        List<Alarm> alarmInfo = alarmDao.findAlarmsByUserAndStateOrderByCreateDateDesc(user,false);
        for(Alarm alarm : alarmInfo){
            alarm.setState(true);
        }
        return ret;
    }

    public AlarmDto.UserAlarm alarmMapping(Article article){
        AlarmDto.UserAlarm ret=  modelMapper.map(article,AlarmDto.UserAlarm.class);
        return ret;
    }
    public AlarmDto.UserAlarm alarmMapping(Alarm alarm){
        AlarmDto.UserAlarm ret=  modelMapper.map(alarm,AlarmDto.UserAlarm.class);
        return ret;
    }
}
