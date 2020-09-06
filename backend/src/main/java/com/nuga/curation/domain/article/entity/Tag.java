package com.nuga.curation.domain.article.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
@NoArgsConstructor
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long TagId;

    @Length(max = 45)
    private String tagName;

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    @Builder
    public Tag(String tagName){
        this.tagName = tagName;
    }
}
