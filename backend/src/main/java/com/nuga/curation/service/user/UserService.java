package com.nuga.curation.service.user;

import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.follow.Follow;
import com.nuga.curation.domain.follow.FollowInfo;
import com.nuga.curation.domain.user.dto.*;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.exception.UnDefinedUserException;
import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.repository.alarm.AlarmDao;
import com.nuga.curation.repository.article.ArticleDao;
import com.nuga.curation.repository.user.FollowDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.util.EmailUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserDao userDao;
    private final FollowDao followDao;
    private final AlarmDao alarmDao;
    private final ModelMapper modelMapper;
    private final ArticleDao articleDao;
    //나중에 DuplicateException 만들어서 사용할 에정
    @Transactional
    public Long userSignup(SignupRequest request) throws UnauthorizedException{
        if(duplicateUser(request.getEmail(),request.getNickname())){
            throw new UnauthorizedException("이미 가입된 메일입니다");
        }
        return userDao.save(request.toEntity().DateSetting(LocalDateTime.now())).getUser_id();
    }

    // 이메일 요청시
    public boolean duplicateUser(String email){
        if(userDao.findUserByEmail(email)!=null) return true;
        return false;
    }

    // 회원가입시
    public boolean duplicateUser(String email, String nickname){
        if(userDao.getUserByEmailOrNickName(email,nickname)!=null){
            return true;
        }
        return false;
    }

    public boolean userValid(String email, String password){
        Optional<User> existUser = userDao.findUserByEmailAndPassword(email,password);
        if(existUser.isPresent()) return true;
        return false;
    }

    @Transactional
    public String findPw(PwdModifyRequest request){
        User findUser = userDao.getUserByEmail(request.getEmail());
        findUser.setPassword(request.getPassword());
        userDao.save(findUser);
        return findUser.getEmail();
    }

    public String modify(ModifyRequest request){
        if(!userValid(request.getEmail(),request.getPassword())) {
            throw new UnauthorizedException();
        }
        User user = userDao.getUserByEmail(request.getEmail());
        user.setPassword(request.getPasswordCheck1());
        userDao.save(user);
        return "비밀번호가 정상적으로 변경 되었습니다";
    }
    //userId인 User가 targetId인 User에게 팔로우 행위를 함
    @Transactional
    public void follow(String nickName, String targetNickName) throws Exception {
        try{
            User user = userDao.findUserByNickName(nickName);
            User targetUser = userDao.findUserByNickName(targetNickName);
            Follow follow = followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(user, targetUser);
            if(follow==null) { //팔로우
                Follow relation = new Follow();
                relation.follow(user,targetUser);
                followDao.save(relation);
                //user.addFollow(targetUser);
            }else{  //언팔로우
                //user.deleteFollow(targetUser);
                followDao.delete(follow);
            }
        }catch (Exception e){
            throw new Exception();
        }
    }

    public FollowResponse followMapping(User user){
        FollowResponse response = modelMapper.map(user,FollowResponse.class);
        return response;
    }

    public boolean isFollow(User user, User targetUser){
        if(followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(user,targetUser)==null ||
        followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(targetUser,user)==null) return false;
        return true;
    }

    //userId인 유저가 팔로우하는 팔로잉리스트
    public List<FollowResponse> followingUsers(User target,Object loginUser){
        Set<User> list = target.getFollowing();
        return getFollowResponses(loginUser, list,target);
    }

    //userId인 유저를 팔로우하는 팔로워리스트
    public List<FollowResponse> followedUsers(User user,Object loginUser){
        Set<User> list = user.getFollowers();
        return getFollowResponses(loginUser, list,user);
    }

    private List<FollowResponse> getFollowResponses(Object loginUser, Set<User> list,User target) {
        User filterUser = (loginUser!=null)?userDao.findUserByNickName(String.valueOf(loginUser)):null;
        List<FollowResponse> followedList;
        if(filterUser!=null) {
            followedList =
                    list.stream()
                            .map(this::followMapping)
                            .filter(s -> (!s.getNickName().equals(filterUser.getNickName())))
                            .collect(Collectors.toList());
        }else{
            followedList =
                    list.stream()
                            .map(this::followMapping)
                            .collect(Collectors.toList());
        }
        if(filterUser!=null && target.getNickName()== filterUser.getNickName()) {
            return followedList;
        }
        for(FollowResponse u: followedList){
            if(filterUser==null){
                u.setFollowed(false);
            }else{
                User targetUser = userDao.findUserByNickName(u.getNickName());
                if(isFollow(filterUser,targetUser)){
                    u.setFollowed(true);
                }else{
                    u.setFollowed(false);
                }
            }
        }
        return followedList;
    }

    public User signIn(String email, String password){
        User user = userDao.getUserByEmail(email);
        Objects.requireNonNull(user, "UNAUTHORIZED");
        if(!user.getPassword().equals(password)){
            throw new IllegalStateException("UNAUTHORIZED");
        }
        return user;
    }

    public LoginUserDto.LoginUserResponse userLoginInfo(User user){
        LoginUserDto.LoginUser userInfo = modelMapper.map(user,LoginUserDto.LoginUser.class);
        List<Alarm> alarmInfo = alarmDao.findAlarmsByUserAndStateOrderByCreateDateDesc(user,false);
        Boolean isHaveAlarm = (alarmInfo.size()>0)?true:false;
        return new LoginUserDto.LoginUserResponse(userInfo,isHaveAlarm);
    }

    private LoginUserDto.LoginAlarm alarmMapping(Alarm alarm) {
        LoginUserDto.LoginAlarm response = modelMapper.map(alarm,LoginUserDto.LoginAlarm.class);
        response.setArticleId(alarm.getArticle().getArticleId());
        return response;
    }
    public List<SearchUserResponse> searchAll(String nickname){
        List<User> users = userDao.findUserByNickNameContaining(nickname);
        List<SearchUserResponse> nicknames = new ArrayList<>();
        for(User user:users){
            SearchUserResponse searchUserResponse = SearchUserResponse
                    .builder()
                    .email(user.getEmail())
                    .nickName(user.getNickName())
                    .build();
            nicknames.add(searchUserResponse);
        }
        return nicknames;
    }
    public LoginUserDto.UserProfile userProfile(String targetNickName,Object userNickName) throws Exception{
        User targetUser = userDao.findUserByNickName(targetNickName);
        LoginUserDto.LoginUser userInfo = modelMapper.map(targetUser,LoginUserDto.LoginUser.class);
        int followingCount = followDao.getFollowsByFollowInfo_User(targetUser).size();
        int followerCount = followDao.getFollowsByFollowInfo_Target(targetUser).size();
        int sellingCount = articleDao.findArticlesBySellerAndExpireDateLessThanOrderByExpireDate(targetUser,LocalDateTime.now()).size();
        int totalSellCount = articleDao.findArticlesBySellerOrderByExpireDate(targetUser).size();
        LoginUserDto.UserProfile profile= new LoginUserDto.UserProfile(userInfo,sellingCount,followerCount,followingCount,totalSellCount,false);
        if(userNickName!=null){ //로그인 유저라면
            User user = userDao.findUserByNickName((String)userNickName);
            Follow followInfo = followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(targetUser,user);
            Follow followInfo2 = followDao.getFollowByFollowInfo_UserAndFollowInfo_Target(user,targetUser);
            if(followInfo!=null  || followInfo2!=null){
                profile.follow();
            }
        }
        return profile;

    }

    @Transactional
    public Long refillCoin(Long userId, Long pay){
        User user = userDao.findById(userId).get();
        user.addCoin(pay);
        return pay;
    }
}
