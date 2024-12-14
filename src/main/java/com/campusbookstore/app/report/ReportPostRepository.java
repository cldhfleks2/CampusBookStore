package com.campusbookstore.app.report;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReportPostRepository  extends JpaRepository<ReportPost, Long> {
    // 1. 메서드 이름 기반 쿼리
    List<ReportPost> findByMemberNameAndPostId(String memberName, Long postId);
}
