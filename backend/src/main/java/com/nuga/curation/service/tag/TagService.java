package com.nuga.curation.service.tag;

import com.nuga.curation.domain.article.entity.Article;
import com.nuga.curation.domain.article.dto.TagRequest;

import java.util.List;

public interface TagService {
    //태그등록
    public void addTagList(List<TagRequest> tagList);
    public void addTagToArticle(Article request, List<TagRequest> tagList);
}
