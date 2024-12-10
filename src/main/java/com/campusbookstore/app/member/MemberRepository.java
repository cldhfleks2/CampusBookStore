package com.campusbookstore.app.member;

import com.campusbookstore.app.review.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByName(String name);

    @Query("SELECT r FROM Review r WHERE r.status = 1")
    List<Review> findAllByStatus();
}
