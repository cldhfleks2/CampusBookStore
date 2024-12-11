package com.campusbookstore.app.image;

import com.campusbookstore.app.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);

    @Query("SELECT r FROM Review r WHERE r.status = 1")
    List<Review> findAllByStatus();


}
