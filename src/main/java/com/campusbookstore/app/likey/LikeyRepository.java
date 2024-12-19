package com.campusbookstore.app.likey;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.purchase.Purchase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LikeyRepository extends JpaRepository<Likey, Long> {
//    @Query("SELECT l FROM Likey l WHERE l.status = 1")
//    List<Likey> findAllByStatus();

    @Query("SELECT l FROM Likey l WHERE l.status = 1 AND l.post.id = :postId")
    List<Likey> findAllByPostIdAndStatus(Long postId);

    @Query("SELECT l FROM Likey l WHERE l.post.id = :postId AND l.status = 1")
    List<Likey> findAllByPostId(Long postId);

    List<Likey> findAllByPostIdAndMemberName(Long postId, String memberName);

    //멤버아이디로 찜한 내역을 가져옴.
    @Query("SELECT l FROM Likey l WHERE l.member.id = :memberId AND l.status = 1")
    Page<Likey> findAllByMemberId(long memberId, Pageable pageable);
}
