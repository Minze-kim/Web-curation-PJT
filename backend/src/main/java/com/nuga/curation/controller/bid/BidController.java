package com.nuga.curation.controller.bid;


import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nuga.curation.domain.article.dto.ArticleDto.ArticleResponse;
import com.nuga.curation.domain.article.dto.BidRequest;
import com.nuga.curation.domain.article.dto.BidTryResponse;
import com.nuga.curation.exception.BidAccessException;
import com.nuga.curation.exception.EndTimeException;
import com.nuga.curation.exception.NotEnoughCoinException;
import com.nuga.curation.exception.SameTimeAccessException;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.service.bid.BidService;
import com.nuga.curation.service.user.JwtService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = { "*" })
@RestController
@RequiredArgsConstructor
public class BidController {
    private final JwtService jwtService;
    private final BidService bidService;
    @PostMapping("/bids")
    @ApiOperation(value = "입찰하기")
    public Object bidTry(@RequestBody BidRequest request){
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            bidService.bidTry(userId,request);
        }catch (NotEnoughCoinException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (SameTimeAccessException se){
            return new ResponseEntity<>(se.getMessage(),HttpStatus.UNAUTHORIZED);
        }catch(EndTimeException ee){
            return new ResponseEntity<>(ee.getMessage(),HttpStatus.PAYMENT_REQUIRED);
        }catch (UnauthorizedException ua){
            return new ResponseEntity<>(HttpStatus.LOCKED);
        } catch(Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("입찰에성공했습니다",HttpStatus.OK);
    }
    @GetMapping("/bids")
    @ApiOperation(value = "입찰시도중인상품")
    public ResponseEntity<List<ArticleResponse>> bidTryArticles(){
        List<ArticleResponse> bidTryResponses;
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            bidTryResponses = bidService.bidTryArticles(userId);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(bidTryResponses, HttpStatus.OK);
    }

    @Scheduled(fixedRate = 1000*60*60)  //일단 테스트 1000*60*60 = 1시간
    public void EndedArticleAlarmSave(){
        bidService.bidUpdate();
    }

    @GetMapping("/bids/receive/{articleId}")
    @ApiOperation(value = "상품수령")
    public Object receiveProduct(@PathVariable Long articleId){
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            bidService.receiveProduct(userId,articleId);
        } catch (BidAccessException e) {
            return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }catch (UnsupportedEncodingException ue){
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(articleId,HttpStatus.OK);
    }
}
