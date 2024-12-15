package com.campusbookstore.app.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r WHERE r.status = 1 AND r.post.id = :postId")
    List<Review> findAllByPostId(@Param("postId") Long postId);
}
