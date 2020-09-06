package com.nuga.curation.controller.articles;


import com.nuga.curation.domain.article.dto.ArticleDto;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.service.article.ArticleService;
import com.nuga.curation.service.user.JwtService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin(origins = { "*" })
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final JwtService jwtService;
    private final ArticleService articleService;
    private final UserDao userDao;
    @PostMapping("/articles")
    @ApiOperation(value = "글쓰기")
    public Object writeArticle(@RequestBody ArticleDto.ArticleWriteRequest articleWriteRequest) {
        try {
            long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            Long articleId = articleService.writeArticleReceiver(userId, articleWriteRequest);
            return new ResponseEntity<>(articleId,HttpStatus.OK);
        } catch (UnauthorizedException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }catch (IOException ioException){ //이미지 파일 에러
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/articles/all")
    @ApiOperation(value = "글전체보기")
    public Object articleList(){
        List<ArticleDto.ArticleResponse> articleList;
        if(jwtService.isHaveToken()){
            try {
                Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
                articleList = articleService.articleList(userId,"");
                return new ResponseEntity<>(articleList,HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        articleList = articleService.articleList();
        return new ResponseEntity<>(articleList,HttpStatus.OK);
    }

    @GetMapping("/articles/detail/{articleId}")
    @ApiOperation(value = "글상세보기")
    public Object articleDetail(@PathVariable Long articleId){
        Long userId = null;
        if(jwtService.isHaveToken()){
            try {
                userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return new ResponseEntity<>(articleService.articleDetail(articleId,userId),HttpStatus.OK);
    }

    @GetMapping("/articles/{nickName}")
    @ApiOperation(value = "유저판매상품전체보기")
    public ResponseEntity<List<ArticleDto.ArticleResponse>> myArticles(@PathVariable String nickName){
        List<ArticleDto.ArticleResponse> myList= new ArrayList<ArticleDto.ArticleResponse>();
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            //로그인 유저가 프로필보기를 할때 (1. 본인프로필 보기, 2. 타유저프로필 보기)
            myList = articleService.myArticles(nickName);
        } catch (UnsupportedEncodingException e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(myList,HttpStatus.OK);
    }

    @GetMapping("/articles/search/{keyword}")
    @ApiOperation(value = "메인피드검색")
    public ResponseEntity<ArticleDto.SearchResponse> search(@PathVariable String keyword){
        Long userId = null;
        try{
            if(jwtService.isHaveToken()){
                userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            }
        }catch (UnsupportedEncodingException e){
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return new ResponseEntity<>(articleService.searchArticle(keyword,userId),HttpStatus.OK);
    }

    @DeleteMapping("/articles/{articleId}")
    @ApiOperation(value = "글삭제(lando94@naver.com만 가능)")
    public Object deleteArticle(@PathVariable Long articleId){
        Long userId = null;
        try {
            userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            User user = userDao.findById(userId).get();
            if(!user.getEmail().equals("lando94@naver.com")) {
                return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
            }
            boolean suc = articleService.deleteArticle(articleId);
            return new ResponseEntity<>(suc,HttpStatus.OK);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(false,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping("/articles/category/{categoryName}")
    @ApiOperation(value = "카테고리별게시글")
    public ResponseEntity<List<ArticleDto.ArticleResponse>> categoryArticles(@PathVariable String categoryName){
        if(jwtService.isHaveToken()){
            try {
                Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
                return new ResponseEntity<>(articleService.articleList(userId,categoryName),HttpStatus.OK);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }else{
            return new ResponseEntity<>(articleService.categoryArticles(categoryName),HttpStatus.OK);
        }
        return null;
    }
}