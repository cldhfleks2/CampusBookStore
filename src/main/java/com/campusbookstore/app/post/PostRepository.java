package com.campusbookstore.app.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingAndAuthor(String titleKeyword, String authorKeyword);

}
