package com.campusbookstore.app.post;

import com.campusbookstore.app.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    //keyword와 일치하는 title, author, campus, major를 검색
    @Query("SELECT p FROM Post p " +
            "WHERE p.status = 1 " +
            "AND (p.title LIKE %:keyword% " +
                "OR p.author LIKE %:keyword% " +
                "OR p.campus LIKE %:keyword% " +
                "OR p.major LIKE %:keyword%)")
    Page<Post> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    //postId로 status=1인 Post를 검색
    @Query("SELECT p FROM Post p WHERE p.id = :postId AND p.status = 1")
    Optional<Post> findByIdAndStatusActive(@Param("postId") Long postId);

    // 작성 날짜 기준으로 status=1이고 quantity > 0인 게시물 N개를 가져온다.
    // @Query로는 갯수를 선택할 수 없기에, Pageable를 이용한다.
    // @Query("SELECT p FROM Post p WHERE p.status = 1 AND p.quantity > 0 ORDER BY p.createDate DESC")
    // List<Post> findTopNByStatusOrderByCreateDateDesc(Pageable pageable);

    //작성 날짜 기준으로 status=1이고 quantity > 0인 게시물 N개를 가져온다.
    @Query(value = "SELECT * FROM post p " +
            "WHERE p.status = 1 AND p.quantity > 0 " +
            "ORDER BY p.create_date DESC LIMIT :n", nativeQuery = true)
    List<Post> findTopNByStatusOrderByCreateDateDesc(@Param("n") int n);
    
    //memberId로 작성한 게시물 가져옴 : 페이지네이션 사용
    //페이지네이션으로 N개를 가져올 수 도 있음
    @Query("SELECT p FROM Post p WHERE p.member.id = :memberId AND p.status = 1")
    Page<Post> findAllByMemberId(long memberId, Pageable pageable);

    //likey갯수가 많은 순서대로 Post를 가져옴
    //status=1이고 N개를 가져옴
    //LIMIT n을 사용하기 위해 nativeQuery=true로 작성.
    @Query(value = "SELECT p.* FROM Post p " +
            "LEFT JOIN Likey l ON p.id = l.post_id " +
            "WHERE p.status = 1 AND l.status = 1 " +
            "GROUP BY p.id " +
            "ORDER BY COUNT(l.id) DESC LIMIT :n", nativeQuery = true)
    List<Post> findTopPostsByLikeyCount(@Param("n") int n);
}

