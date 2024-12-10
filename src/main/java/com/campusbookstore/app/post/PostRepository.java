package com.campusbookstore.app.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingAndAuthor(String titleKeyword, String authorKeyword);

    //status=1 인것만 가져오는 JPA메소드
    List<Post> findByStatus(int status);
}
