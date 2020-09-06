package com.nuga.curation.classTest;

import com.nuga.curation.service.mail.MailService;

public class EmailUtilTest {
    public static void main(String[] args) throws Exception {
        MailService mailService = new MailService();
        mailService.sendEmailVerification("lando94@naver.com");
    }
}
