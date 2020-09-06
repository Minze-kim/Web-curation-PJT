package com.nuga.curation.controller.wish;

import com.nuga.curation.domain.WishResponseEntity;
import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.service.user.JwtService;
import com.nuga.curation.service.wish.WishService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = { "*" })
@RequiredArgsConstructor
@RestController
public class WishController {

    private final WishService wishService;
    private final JwtService jwtService;

    @PutMapping("/wishs/{articleId}")
    @ApiOperation(value = "상품찜하기")
    public Object wishSave(@PathVariable final Long articleId){
        try {
            long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            WishResponseEntity response = new WishResponseEntity();
            response.message = wishService.wishToArticle(userId,articleId);
            response.articleId = articleId;
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>("본인 상품에는 찜할 수 없습니다",HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/wishs")
    @ApiOperation(value = "찜리스트보기")
    public Object wishList(){
        long userId;
        try {
            userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(wishService.wishList(userId),HttpStatus.OK);
    }

}
