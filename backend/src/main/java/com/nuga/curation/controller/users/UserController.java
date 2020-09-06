package com.nuga.curation.controller.users;


import com.nuga.curation.domain.BasicResponse;
import com.nuga.curation.domain.user.dto.*;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.exception.CodeNotFoundException;
import com.nuga.curation.exception.CodeTimeException;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.service.mail.MailService;
import com.nuga.curation.service.user.JwtService;
import com.nuga.curation.service.user.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;


@ApiResponses(value = { @ApiResponse(code = 401, message = "Unauthorized", response = BasicResponse.class),
        @ApiResponse(code = 403, message = "Forbidden", response = BasicResponse.class),
        @ApiResponse(code = 404, message = "Not Found", response = BasicResponse.class),
        @ApiResponse(code = 500, message = "Failure", response = BasicResponse.class) })

@RequiredArgsConstructor
@CrossOrigin(origins = { "*" })
@RestController
public class UserController {
    private final UserService userService;
    private final UserDao userDao;
    private final JwtService jwtService;
    private final MailService maileService;


    @PostMapping("/users/login")
    @ApiOperation(value = "로그인")
    public Object login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        try{
            User user = userService.signIn(email,password);
            String token = jwtService.create(user);
            response.setHeader("Authorization",token);
            LoginUserDto.LoginUserResponse responses = userService.userLoginInfo(user);
            return new ResponseEntity<>(responses,HttpStatus.OK);
        }catch (Exception e){
            if(e.getMessage().equals("UNAUTHORIZED")){
                return new ResponseEntity<>("로그인 정보가 올바르지 않습니다.",HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/users/signup")
    @ApiOperation(value = "가입하기")
    public Object signup(@Valid @RequestBody SignupRequest request) {
        final BasicResponse result = new BasicResponse();
        try {
            userService.userSignup(request);
        }catch (UnauthorizedException ue){
            return new ResponseEntity<>(ue.getMessage(),HttpStatus.CONFLICT);
        }
        result.data = "success";
        return new ResponseEntity<>(result, HttpStatus.OK);

    }

    @GetMapping("/users/findpw/email/{email}")
    @ApiOperation(value = "비밀번호 찾기 이메일 요청")
    public Object findpwByEmail(@PathVariable String email){
        try {
            if(userService.duplicateUser(email)==false){ //db에 존재하지 않는 이메일이면
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            }
            maileService.sendEmailVerification(email);
        } catch (Exception e) {
            return new ResponseEntity<>("이메일에러",HttpStatus.FORBIDDEN);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/users/findpw/{email}")
    @ApiOperation(value = "비밀번호변경")
    public ResponseEntity<String> findPw(@RequestBody PwdModifyRequest request){
        String message;
        try{
            message = userService.findPw(request);
        }catch (Exception e){
            return new ResponseEntity<>(e.getMessage(),HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(message,HttpStatus.OK);
    }


    @PutMapping("/users")
    @ApiOperation(value = "회원정보변경")
    public ResponseEntity<String> userModify(@Valid @RequestBody ModifyRequest request){
        String msg = "";
        try{
            msg = userService.modify(request);
        }catch (UnauthorizedException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }

    @PostMapping("/users/follow")
    @ApiOperation(value="팔로우하기")
    public Object follow(@RequestBody String targetUserNickName) {
        try {
            String newTargetUserNickName = targetUserNickName.substring(0,targetUserNickName.length() -1);
            String nickName = String.valueOf(jwtService.get("nickName"));
            userService.follow(nickName,newTargetUserNickName);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @GetMapping("/users/follower/{nickName}")
    @ApiOperation(value="팔로워리스트(해당유저를팔로우한사람)")
    public Object followerList(@PathVariable String nickName){
        Object obj = null;
        try {
            if(jwtService.isHaveToken()){
                obj = jwtService.get("nickName");
            }
            User user = userDao.findUserByNickName(nickName);
            List<FollowResponse> list =  userService.followedUsers(user,obj);
            return new ResponseEntity<>(list,HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }
    @GetMapping("/users/following/{nickName}")
    @ApiOperation(value = "팔로우리스트(해당유저가 팔로우한 사람)")
    public Object followingList(@PathVariable String nickName){
        Object obj = null;
        try {
            if(jwtService.isHaveToken()){
                obj = jwtService.get("nickName");
            }
            User user = userDao.findUserByNickName(nickName);
            List<FollowResponse> list =  userService.followingUsers(user,obj);
            return new ResponseEntity<>(list,HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.LOCKED);
        }
    }

    @GetMapping("/users/signup/email/{email}")
    @ApiOperation(value="(email)로 코드번호 전송하기")
    public ResponseEntity<String> emailSend(@PathVariable String email){
        try {
            if(userService.duplicateUser(email)) return new ResponseEntity<>(HttpStatus.CONFLICT);
            maileService.sendEmailVerification(email);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/email/confirm/{email}")
    @ApiOperation(value="코드번호확인")
    public Object emailConfirm(@PathVariable String email, @RequestBody String code){
        //해당 메일의 코드를 db에서 확인, MailAuth의 endTime이 현재시간 이후면 401, MailAuth의 code와 유저가 요청한 code가 다르면 400
        code = code.substring(0,code.length()-1);
        boolean suc = true;
        try{
            suc = maileService.emailConfirm(email,code);
        }catch (CodeTimeException | CodeNotFoundException e){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        if(suc ==false)return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/users/search/{nickname}")
    @ApiOperation(value = "닉네임으로 유저검색")
    public ResponseEntity<List<SearchUserResponse>> userSearch(@PathVariable String nickname) {
        try {
            List<SearchUserResponse> searchList = userService.searchAll(nickname);
            return new ResponseEntity<>(searchList, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/users/profile/{nickName}")
    @ApiOperation(value = "유저프로필")
    public ResponseEntity<LoginUserDto.UserProfile > userProfile(@PathVariable String nickName){
        Object obj = null;
        try{
            if(jwtService.isHaveToken()){
                obj = String.valueOf(jwtService.get("nickName"));
            }
            LoginUserDto.UserProfile profile = userService.userProfile(nickName,obj);
            if(nickName.equals(obj)) profile.follow();
            return new ResponseEntity<>(profile, HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/users/coin")
    @ApiOperation(value = "코인충전하기")
    public String coinRefill(@RequestBody String coin){
        coin = coin.substring(0,coin.length()-1);
        try {
            Long userId = Long.parseLong(String.valueOf(jwtService.get("userId")));
            Long refilledCoin = userService.refillCoin(userId, Long.parseLong(coin));
            return refilledCoin.toString();
        } catch (UnsupportedEncodingException e) {
            new ResponseEntity<>(HttpStatus.LOCKED);
        }
        return null;
    }
}