package com.nuga.curation.domain.article.entity;


import com.nuga.curation.domain.alarm.Alarm;
import com.nuga.curation.domain.category.Category;
import com.nuga.curation.domain.image.Image;
import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.domain.wish.Wish;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JoinColumnOrFormula;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long articleId;

    @ManyToOne
    @JoinColumnOrFormula(column=
            @JoinColumn(name = "seller",updatable = false,insertable = true)
    )
    private User seller;

    @ManyToOne
    @JoinColumn(name = "buyer",updatable = true,insertable = false)
    private  User buyer;

    @NotNull
    private Long price;

    private Long immediatePrice;

    @NotNull
    private Long minUnit;

    @NotNull
    @Length(max=45)
    private String title;

    @Lob
    private String description;

    @CreatedDate
    private LocalDateTime createdDate;

    private LocalDateTime purchaseDate;

    @NotNull
    private LocalDateTime expireDate;

    private int state;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ColumnDefault("0")
    private int wishCount; //좋아요 수

    //글삭제 원활(관리자만 삭제 가능)
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<ArticleTag> articleTags = new ArrayList<ArticleTag>();
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Wish> wishList = new ArrayList<Wish>();
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Alarm> alarmList = new ArrayList<Alarm>();
    @OneToMany(mappedBy = "article", cascade = CascadeType.REMOVE)
    private List<Image> images = new ArrayList<Image>();

    @Builder
    public Article(Long price,String title,User user,Long minUnit,LocalDateTime purchaseDate,Integer state,
                          String description,LocalDateTime expireDate,LocalDateTime createdDate,Long immediatePrice,
                   User buyer,Category category, int wishCount){
        this.price=price;
        this.title = title;
        this.seller = user;
        this.minUnit = minUnit;
        this.purchaseDate = purchaseDate;
        this.state = state;
        this.description = description;
        this.expireDate = expireDate;
        this.createdDate= createdDate;
        this.immediatePrice = immediatePrice;
        this.buyer = buyer;
        this.category = category;
        this.wishCount = wishCount;
    }

    public Article sellerSetting(User seller){
        this.wishCount = 0;
        this.seller = seller;
        return this;
    }
    public Article categorySetting(Category category){
        this.category = category;
        return this;
    }

    public Article createDateSetting(LocalDateTime dateTime){
        this.createdDate = dateTime;
        return this;
    }

    public Article bidInfoUpdate(User buyer,Long updatePrice){
        //기존에 buyer가 있을 경우 마일리지 반환
        User originBuyer = this.buyer;
        if(originBuyer!=null){
            Long originCoin = originBuyer.getCoin();
            originBuyer.setCoin(originCoin + this.price);
        }
        this.buyer = buyer;
        this.price = updatePrice;
        Long coin = buyer.getCoin();
        buyer.setCoin(coin-updatePrice);
        return this;
    }

    public void wishUpdate(int value){
        this.wishCount += value;
    }

    public void expireDateSetting(String day){
        this.expireDate = LocalDateTime.now().plusDays(Long.parseLong(day.substring(0,1)));
    }


    public void sellEnd(){
        User seller = this.getSeller();
        seller.setCoin(seller.getCoin()+this.getPrice());
    }

    @Override
    public Object clone() throws CloneNotSupportedException { // public 으로 바꿔주자.
        return super.clone();
    }
}
