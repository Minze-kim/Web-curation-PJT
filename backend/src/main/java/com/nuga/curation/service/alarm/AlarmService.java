
package com.nuga.curation.service.alarm;

import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.alarm.dto.AlarmDto;
import com.nuga.curation.domain.user.entity.User;
import org.springframework.stereotype.Service;

import java.util.List;


public interface AlarmService {
    public List<AlarmDto.UserAlarm> userAlarm(Long userId);
}