package com.nuga.curation.domain.user;


import com.nuga.curation.domain.user.dto.ModifyRequest;
import com.nuga.curation.domain.user.dto.SignupRequest;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.repository.mail.MailDao;
import com.nuga.curation.repository.user.UserDao;
import com.nuga.curation.exception.CodeNotFoundException;
import com.nuga.curation.exception.CodeTimeException;
import com.nuga.curation.domain.mail.MailAuth;
import com.nuga.curation.service.mail.MailService;
import com.nuga.curation.service.user.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.mail.MessagingException;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest {

    @Autowired
    UserDao userDao;

    @Autowired
    UserService userService;

    @Autowired
    MailService mailService;

    @Autowired
    MailDao mailDao;

    @Test
    public void sign_Up_Test(){
        String email = "lando94@naver.com";
        String nickname = "hando2";
        String password = "ssafy123!23";

        SignupRequest signUser = SignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();

        User searchUser = userDao.getUserByEmailOrNickName(email,nickname);
        assertThat(searchUser).isEqualTo(null);
        Long lastInsertId = userDao.save(signUser.toEntity().DateSetting(LocalDateTime.now())).getUser_id();

        User testUser = userDao.findById(lastInsertId).get();
        assertThat(testUser.getEmail()).isEqualTo(email);
        assertThat(testUser.getNickName()).isEqualTo(nickname);
        assertThat(testUser.getPassword()).isEqualTo(password);
        System.out.println(testUser.getCreateDate());

    }

    //미리 db에 넣어둔 email,password를 보고 확인한 테스트
    //sign-up 테스트가 완료되면 save와 find를 동시 테스트 할 것
    @Test
    public void join_and_login(){
        String email = "lando94@naver.com";
        String password = "ssafy";

        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        userDao.save(user);
        Optional<User> userOpt = userDao.findUserByEmailAndPassword(email,password);
        User findUser = userOpt.get();
        System.out.println(findUser.getEmail());

        assertThat(findUser.getEmail()).isEqualTo(email);
        assertThat(findUser.getPassword()).isEqualTo(password);
    }

    //나중에 DuplicateException 만들어서 사용할 에정
    @Test(expected = Exception.class)
    public void sign_Up_Conflic_Test() throws Exception{
        String email = "aaa@naver.com";
        String nickname = "hando";
        String password = "ssafy";

        SignupRequest signUser = SignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
        Long oldId = userService.userSignup(signUser);

        SignupRequest signUser2 = SignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(password)
                .build();
        Long lastInsertId = userService.userSignup(signUser2);

        System.out.println("출력되면 안되는 문장");

    }


    @Test
    public void user_Modify(){
        String successMsg = "비밀번호가 정상적으로 변경 되었습니다";
        String failMsg = "회원 정보가 올바르지 않습니다";

        String email = "lando94@naver.com";
        String firstpassword = "ssafy123!23";
        String nickname = "lando";

        SignupRequest signUser = SignupRequest.builder()
                .email(email)
                .nickname(nickname)
                .password(firstpassword)
                .build();

        Long lastInsertId = userDao.save(signUser.toEntity().DateSetting(LocalDateTime.now())).getUser_id();

        String secondPassword = "lando94!23";

        String mEmail = "lando94@naver.com";
        String checkPassword = new String(firstpassword);
        String modifingPassword = "lando94!23";  //변경될 비밀번호

        ModifyRequest request = ModifyRequest.builder()
                .email(mEmail)
                .password(checkPassword)
                .modifyPass(modifingPassword)
                .build();

        String ret = userService.modify(request);
        User modifiedUser = userDao.findById(lastInsertId).get();

        assertThat(ret).isEqualTo(successMsg);
        assertThat(modifiedUser.getEmail()).isEqualTo(email);
        assertThat(modifiedUser.getPassword()).isEqualTo(modifingPassword);
    }

    @Test
    public void mail_test() throws CodeNotFoundException, CodeTimeException {
        //보내기
        String mail = "lando94@naver.com";
        try {
            mailService.sendEmailVerification(mail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        MailAuth mailAuth = mailDao.findTopByMailOrderByEndTimeDesc(mail);
        //검증하기
        String code = mailAuth.getCode();
        System.out.println("생성됐던 코드 : " + code);
        mailService.emailConfirm(mail,code);
        assertThat(true);
    }
}
