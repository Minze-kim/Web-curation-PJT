package com.nuga.curation.controller.alarms;

import com.nuga.curation.domain.alarm.dto.AlarmDto;
import com.nuga.curation.service.alarm.AlarmService;
import com.nuga.curation.service.user.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.*;


@RequiredArgsConstructor
@RestController
public class AlarmController {
    private final AlarmService alarmService;
    private final JwtService jwtService;

    @GetMapping("/alarms")
    @ApiOperation(value = "해당유저알람")
    public ResponseEntity<List<AlarmDto.UserAlarm>> userAlarms(){
        List<AlarmDto.UserAlarm> response = new ArrayList<AlarmDto.UserAlarm>();
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            response = alarmService.userAlarm(userId);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
