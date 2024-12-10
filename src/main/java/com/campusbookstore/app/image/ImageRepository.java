package com.campusbookstore.app.image;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findByPostId(Long postId);

    //status=1 인것만 가져오는 JPA메소드
    List<Image> findByStatus(int status);
}
