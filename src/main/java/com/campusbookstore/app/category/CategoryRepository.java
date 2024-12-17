package com.campusbookstore.app.category;

import com.campusbookstore.app.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c WHERE c.post.id = :postId")
    List<Category> findAllByPostId(@Param("postId") Long postId);

    //카테고리 이름으로 검색 : 정확히 일치하는것
    @Query("SELECT c FROM Category c WHERE c.name = :categoryName")
    List<Category> findAllByCategoryName(@Param("categoryName") String categoryName);

    //카테고리 이름으로 검색 : 부분 일치 포함
    @Query("SELECT c FROM Category c WHERE c.name LIKE %:keyword%")
    List<Category> findByKeyword(@Param("keyword") String keyword);
}
