package com.nuga.curation.service.mail;
import com.nuga.curation.repository.mail.MailDao;
import com.nuga.curation.exception.CodeNotFoundException;
import com.nuga.curation.exception.CodeTimeException;
import com.nuga.curation.domain.mail.MailAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class MailService {

    @Autowired
    private JavaMailSenderImpl sender;

    @Autowired
    private MailDao mailDao;

    
    @Transactional
    public void sendEmailVerification(String email) throws MessagingException {
        Random r = new Random();
        String code = Integer.toString(r.nextInt(500000) + 364812);
        MimeMessage message = sender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message);
        try {
            helper.setTo(email);
            helper.setText("가입 코드 : " + code);
            helper.setSubject("회원인증코드입니다");
        } catch (MessagingException e) {
            throw new MessagingException();
        }
        sender.send(message);
        MailAuth mailAuth = MailAuth.builder().mail(email).code(code).build();
        mailAuth.setEndTime(LocalDateTime.now().plusMinutes(30)); //30분의 인증 가능 시간
        mailDao.saveAndFlush(mailAuth);
    }

    public boolean emailConfirm(String email,String code) throws CodeTimeException, CodeNotFoundException {
        MailAuth auth = mailDao.findTopByMailOrderByEndTimeDesc(email);
        if(auth.getEndTime().isBefore(LocalDateTime.now())){ //만료된 인증키라면
            throw new CodeTimeException();
        }
        if(!auth.getCode().equals(code)){ //인증키가 틀렸으면
            throw new CodeNotFoundException();
        }
        return true;
    }
}


