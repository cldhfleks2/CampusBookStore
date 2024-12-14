package com.campusbookstore.app.report;

import com.campusbookstore.app.member.Member;
import com.campusbookstore.app.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@SQLDelete(sql = "UPDATE reportPost SET status = 0, create_date = CURRENT_TIMESTAMP WHERE id = ?")
@ToString
public class ReportPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean inappropriateContent; // 부적절한 내용
    private boolean spamOrAds;           // 스팸 또는 광고
    private boolean copyrightInfringement; // 저작권 침해
    private boolean misinformation;      // 허위 정보
    private String otherReason;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createDate;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post; //어떤 게시물

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member; //어떤 유저

    //찜한 내역 존재 여부
    private Integer status=1;
}

