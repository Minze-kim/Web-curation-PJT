package com.nuga.curation.domain.article.dto;

import com.nuga.curation.domain.article.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TagRequest {
    private String tagName;

    public Tag toEntity(){
        return Tag.builder().tagName(tagName).build();
    }
}
