package com.nuga.curation.repository.article;

import com.nuga.curation.domain.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryDao extends JpaRepository<Category,Long> {
    Category getCategoryByCategoryName(String categoryName);
}
