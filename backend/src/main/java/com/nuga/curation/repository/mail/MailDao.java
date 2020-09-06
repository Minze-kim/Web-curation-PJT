package com.nuga.curation.repository.mail;

import com.nuga.curation.domain.mail.MailAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailDao  extends JpaRepository<MailAuth,Long>{
    MailAuth getByMail(String email);
    MailAuth findTopByMailOrderByEndTimeDesc(String email);
}
