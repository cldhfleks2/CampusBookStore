package com.campusbookstore.app.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.post.id = :postId")
    List<Category> findAllByPostId(@Param("postId") Long postId);

    //카테고리 이름으로 검색
    @Query("SELECT c FROM Category c WHERE c.name = :categoryName")
    List<Category> findAllByCategoryName(@Param("categoryName") String categoryName);
}
