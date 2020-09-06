package com.nuga.curation.domain.article.dto;


import com.nuga.curation.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BidTryResponse {
    private Long articleId;
    private Long price;
    private Long immediatePrice;
    private LocalDateTime expireDate;
    private String buyer;
    private String title;

    //1. 입찰시도하는 사람에 대한 알람
    //2. 입찰실패한 사람에 대한 알람
    //3. 입찰에 성공한 구매자에 대한 알람
    private Integer state;

    public void setState(Integer state){
        this.state = state;
    }
    public void setBuyer(User user){
        this.buyer = user.getNickName();
    }
}
