package com.nuga.curation.domain.mail;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class MailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mail;

    private String code;

    private LocalDateTime endTime; //코드 유효 시간

    @Builder
    public MailAuth(String mail,String code){
        this.mail = mail;
        this.code = code;
    }
}
