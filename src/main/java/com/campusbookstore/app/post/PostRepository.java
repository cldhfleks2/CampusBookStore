package com.campusbookstore.app.post;

import com.campusbookstore.app.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingAndAuthor(String titleKeyword, String authorKeyword);

    @Query("SELECT r FROM Review r WHERE r.status = 1")
    List<Review> findAllByStatus();

    //작성 날짜 기준 n개를 가져옴
    @Query("SELECT p FROM Post p WHERE p.status = 1 ORDER BY p.createDate DESC")
    List<Post> findTopNByStatusOrderByCreateDateDesc(int n);

    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId AND p.status = 1")
    Page<Post> findAllByMemberId(long memberId, Pageable pageable);
}

