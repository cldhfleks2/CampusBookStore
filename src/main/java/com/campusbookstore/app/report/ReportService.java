package com.campusbookstore.app.report;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
import com.campusbookstore.app.review.Review;
import com.campusbookstore.app.review.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportPostRepository reportPostRepository;
    private final ReportReviewRepository reportReviewRepository;
    private final PostRepository postRepository;
    private final ReviewRepository reviewRepository;

    String viewLogin(){
        return "admin/adminLogin";
    }

    String viewReport(Model model){
        List<ReportPost> reportPosts = reportPostRepository.findAllAndStatusActive();
        List<ReportReview> reportReviews = reportReviewRepository.findAllAndStatusActive();

        if(!reportPosts.isEmpty())
            model.addAttribute("reportPosts", reportPosts);
        if(!reportReviews.isEmpty())
            model.addAttribute("reportReviews", reportReviews);
        return "admin/report";
    }

    //reportId들을 받아서 reportPost 삭제
    @Transactional
    ResponseEntity<String> ignorePost(List<Long> reportIds) {
        for (Long reportId : reportIds) {
            //유효성 검사
            //1. reportPost 존재 여부
            Optional<ReportPost> reportPostObj = reportPostRepository.findById(reportId);
            if(!reportPostObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/post/ignore", "reportPostId에 해당하는 게시물 신고내역이 없습니다.", ResponseEntity.class);
                continue;
            }
            //2. reportPost에 해당하는 post 존재 여부
            Optional<Post> postObj = postRepository.findById(reportPostObj.get().getPost().getId());
            if(!postObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/post/ignore", "신고내역과 일치하는 게시물이 없습니다.", ResponseEntity.class);
                continue;
            }
            //DB저장 : 신고내역 soft삭제
            ReportPost reportPost = reportPostObj.get();
            reportPost.setStatus(0); //감추기
            reportPostRepository.save(reportPost);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //reportId들을 받아서 reportPost에 해당하는 post를 실제로 삭제
    @Transactional
    ResponseEntity<String> deletePost(List<Long> reportIds) {
        for (Long reportId : reportIds) {
            //유효성 검사
            Optional<ReportPost> reportPostObj = reportPostRepository.findById(reportId);
            if(!reportPostObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/post/delete", "reportPostId에 해당하는 게시물이 없습니다.", ResponseEntity.class);
                continue;
            }
            //2. reportPost에 해당하는 post 존재 여부
            Optional<Post> postObj = postRepository.findById(reportPostObj.get().getPost().getId());
            if(!postObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/post/delete", "신고내역과 일치하는 게시물이 없습니다.", ResponseEntity.class);
                continue;
            }
            //DB저장 : 신고내역 soft삭제
            ReportPost reportPost = reportPostObj.get();
            reportPost.setStatus(0);
            reportPostRepository.save(reportPost);
            
            //DB저장 : 게시물 soft삭제
            Post post = postObj.get();
            post.setStatus(0);
            postRepository.save(post);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }





    //reportId들을 받아서 reportReview 삭제
    @Transactional
    ResponseEntity<String> ignoreReview(List<Long> reportIds) {
        for (Long reportId : reportIds) {
            //유효성 검사
            //1. reportReview 존재 여부
            Optional<ReportReview> reportReviewObj = reportReviewRepository.findById(reportId);
            if(!reportReviewObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/review/ignore", "reportReviewId에 해당하는 댓글 신고내역이 없습니다.", ResponseEntity.class);
                continue;
            }
            //2. reportReview에 해당하는 review 존재 여부
            Optional<Review> reviewObj = reviewRepository.findById(reportReviewObj.get().getReview().getId());
            if(!reviewObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/review/ignore", "신고내역과 일치하는 댓글이 없습니다.", ResponseEntity.class);
                continue;
            }
            //DB저장 : 신고내역 soft삭제
            ReportReview reportReview = reportReviewObj.get();
            reportReview.setStatus(0); //감추기
            reportReviewRepository.save(reportReview);
        }

        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    //reportId들을 받아서 reportReview에 해당하는 Review를 실제로 삭제
    @Transactional
    ResponseEntity<String> deleteReview(List<Long> reportIds) {
        for (Long reportId : reportIds) {
            //유효성 검사
            Optional<ReportReview> reportReviewObj = reportReviewRepository.findById(reportId);
            if(!reportReviewObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/review/delete", "reportReviewId에 해당하는 댓글 신고내역이 없습니다.", ResponseEntity.class);
                continue;
            }
            //2. reportReview에 해당하는 review 존재 여부
            Optional<Review> reviewObj = reviewRepository.findById(reportReviewObj.get().getReview().getId());
            if(!reviewObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/review/delete", "신고내역과 일치하는 댓글이 없습니다.", ResponseEntity.class);
                continue;
            }
            //DB저장 : 신고내역 soft삭제
            ReportReview reportReview = reportReviewObj.get();
            reportReview.setStatus(0);
            reportReviewRepository.save(reportReview);

            //DB저장 : 댓글 soft삭제
            Review review = reviewObj.get();
            review.setStatus(0);
            reviewRepository.save(review);
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }



}
