package com.campusbookstore.app.purchase;

import com.campusbookstore.app.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    //유저id로 주문 엔티티를 가져옴 : 페이지네이션
    @Query("SELECT p FROM Purchase p WHERE p.member.id = :memberId AND p.status = 1")
    Page<Purchase> findAllByMemberId(long memberId, Pageable pageable);
}
