package com.campusbookstore.app.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportPostRepository  extends JpaRepository<ReportPost, Long> {
    //memberName과 postId에 해당하는 모든 신고내역을 가져옴
    List<ReportPost> findAllByMemberNameAndPostId(String memberName, Long postId);

    //status=1인 모든 신고내역을 날짜순 정렬해서 가져옴
    @Query("SELECT r FROM ReportPost r WHERE r.status = 1 ORDER BY r.createDate DESC")
    List<ReportPost> findAllAndStatusActive();
}
