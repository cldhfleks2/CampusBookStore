package com.campusbookstore.app.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    //status = 1 인것만 가져오는 JPA메소드   자동으로 where status=1 조건을 추가해줌
    @Query("SELECT r FROM Review r WHERE r.status = 1")
    List<Review> findAllByStatus();
}
