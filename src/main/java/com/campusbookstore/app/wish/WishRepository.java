package com.campusbookstore.app.wish;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishRepository extends JpaRepository<Wish, Long> {
    //유저이름으로 장바구니 데이터를 가져옴
    @Query("SELECT w FROM Wish w WHERE w.member.name = :memberName")
    List<Wish> findAllByMemberName(@Param("memberName") String memberName);
}
