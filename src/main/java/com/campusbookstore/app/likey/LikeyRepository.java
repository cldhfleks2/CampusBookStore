package com.campusbookstore.app.likey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LikeyRepository extends JpaRepository<Likey, Long> {
    @Query("SELECT l FROM Likey l WHERE l.status = 1")
    List<Likey> findAllByStatus();

    @Query("SELECT l FROM Likey l WHERE l.post.id = :postId AND l.status = 1")
    List<Likey> findAllByPostId(Long postId);
}
