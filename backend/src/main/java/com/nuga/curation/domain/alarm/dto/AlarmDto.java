package com.nuga.curation.domain.alarm.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class AlarmDto {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserAlarm{
        private Long articleId;
        private String title;

        //1. 입찰시도하는 사람에 대한 알람
        //2. 입찰실패한 사람에 대한 알람
        //3. 입찰에 성공한 구매자에 대한 알람
        //4. 판매상품에 대한알람(구매자가 있을경우)
        //5. 판매상품에 대한알람(구매자가 없을경우)
        //6. 등록한 관심피드 리스트에 해당하는 글이 올라왔을 경우에 대한 알람
        //7. 구매자가 물품 수령 시 판매자에게 가는 알람
        private Integer state;
        @Builder
        public UserAlarm(Long articleId, Integer state, String title){
            this.articleId = articleId;
            this.state = state;
            this.title= title;
        }
    }
}
