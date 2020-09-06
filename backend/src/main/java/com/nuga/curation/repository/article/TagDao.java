package com.nuga.curation.repository.article;

import com.nuga.curation.domain.article.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagDao extends JpaRepository<Tag,Long> {
    Tag findByTagName(String tagName);
}
