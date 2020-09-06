package com.nuga.curation.domain.alarm;


import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Alarm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long alarmId;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @CreatedDate
    @Column(columnDefinition = "DATETIME(3) DEFAULT CURRENT_TIMESTAMP(3)", insertable = false, updatable = false)
    private LocalDateTime createDate;

    private boolean state; //false면 읽지 않음, true면 읽음

    //1. 입찰시도하는 사람에 대한 알람
    //2. 입찰실패한 사람에 대한 알람
    //3. 판매상품에 대한알람(구매자가 있을경우)
    //4. 판매상품에 대한알람(구매자가 없을경우)
    //5. 등록한 관심피드 리스트에 해당하는 글이 올라왔을 경우에 대한 알람
    //6. 등록한 관심피드 리스트에 해당하는 글이 올라왔을 경우에 대한 알람
    // 7. 구매자가 물품 수령 시 판매자에게 가는 알람
    private Integer kind;

    @Builder
    public Alarm(Article article, User user, Boolean state,Integer kind){
        this.article = article;
        this.user = user;
        this.state = state;
        this.kind = kind;
    }

    public void setState(boolean state){
        this.state = state;
    }
    public void setDateTime(LocalDateTime localDateTime){
        this.createDate = localDateTime;
    }

    @Override
    public String toString() {
        return "Alarm{" +
                "alarmId=" + alarmId +
                ", article=" + article.getTitle() +
                ", user=" + user.getNickName() +
                ", createDate=" + createDate +
                ", state=" + state +
                ", kind=" + kind +
                '}';
    }
}
