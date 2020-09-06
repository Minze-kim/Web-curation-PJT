package com.nuga.curation.domain.article.dto;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.user.dto.SearchUserResponse;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


public class ArticleDto {

    @Getter
    public static class ArticleRequest{
        private Long price;
        private String title;
        private Long minUnit;
        private String purchaseDate;
        private Integer state;
        private String description;
        private String expireDate;
        private Long immediatePrice;
        @Builder
        public ArticleRequest(Long price, String title, Long minUnit, String purchaseDate, Integer state,
                              String description, String expireDate, Long immediatePrice){
            this.price=price;
            this.title = title;
            this.minUnit = minUnit;
            this.purchaseDate = purchaseDate;
            this.state = state;
            this.description = description;
            this.expireDate = expireDate;
            this.immediatePrice = immediatePrice;
        }
        public Article toEntity(){
            return Article.builder()
                    .price(price).title(title).minUnit(minUnit)
                    .state(state).description(description).immediatePrice(immediatePrice)
                    .build();
        }
    }

    @Getter @Setter
    @AllArgsConstructor
    public static class ArticleWriteRequest{
        ArticleDto.ArticleRequest articleRequest;
        List<TagRequest> tagRequests;
        String categoryName;
        List<ImageDto> images;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public static class WishResponseToUser{
        private Long articleId;
        private Long price;
        private Long immediatePrice;
        private LocalDateTime expireDate;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ArticleResponse{
        private Long articleId;
        private Long sellerId;
        private String sellerNickname;
        private String buyerNickname;
        private Long price;
        private Long immediatePrice;
        private Long minUnit;
        private String title;
        private String description;
        private LocalDateTime purchaseDate;
        private LocalDateTime expireDate;
        private LocalDateTime createdDate;
        private int state;
        private String categoryName;
        private int wishCount;
        private boolean wished;
        private boolean followed;
        private List<String> images;
        private List<String> tags;
        public ArticleResponse setWished(boolean wished){
            this.wished = wished;
            return this;
        }
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class SearchResponse{
        private List<SearchUserResponse> searchUser;
        private List<ArticleResponse> searchArticle;
    }


    @NoArgsConstructor
    @Getter
    @Setter
    public static class ImageDto{
        private String urlPath;
        @Builder
        public ImageDto(String urlPath){
            this.urlPath = urlPath;
        }
    }
}
