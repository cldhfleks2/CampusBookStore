package com.campusbookstore.app.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingAndAuthor(String titleKeyword, String authorKeyword);

    //status=1 인것만 가져오는 JPA메소드
    List<Post> findByStatus(int status);

    //작성 날짜 기준 n개를 가져옴
//    List<Post> findTopNByStatusOrderByCreateDateDesc(int n);
    @Query("SELECT p FROM Post p WHERE p.status = 1 ORDER BY p.createDate DESC")
    List<Post> findTopNByStatusOrderByCreateDateDesc(int n);


}

