package com.nuga.curation.controller.enterestfeed;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nuga.curation.service.feed.FeedService;
import com.nuga.curation.service.user.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.util.*;


@JsonIgnoreProperties(value={"hibernateLazyInitializer", "handler"})
@RequiredArgsConstructor
@RestController
public class EnterestFeedController {
    private final FeedService feedService;
    private final JwtService jwtService;

    @GetMapping("/wishfeed")
    @ApiOperation(value = "유저관심피드리스트")
    public ResponseEntity<List<String>> feedList(){
        List<String> feedList = new ArrayList<String>();
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            feedList = feedService.feedList(userId);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(feedList, HttpStatus.OK);
    }

    @PostMapping("/wishfeed")
    @ApiOperation(value = "유저관심피드추가")
    public Object addFeed(@RequestBody List<String> feedList){
        if(feedList.size()>=10) return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            feedService.addFeed(userId,feedList);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
