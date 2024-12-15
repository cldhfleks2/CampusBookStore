package com.campusbookstore.app.report;

import com.campusbookstore.app.error.ErrorService;
import com.campusbookstore.app.post.Post;
import com.campusbookstore.app.post.PostRepository;
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

    String viewLogin(){
        return "admin/adminLogin";
    }

    String viewReport(Model model){
        List<ReportPost> reportPosts = reportPostRepository.findAllAndStatusActive();

        model.addAttribute("reportPosts", reportPosts);
        return "admin/report";
    }

    //reportId들을 받아서 reportPost 삭제
    @Transactional
    ResponseEntity<String> ignoreReport(List<Long> reportIds) {
        for (Long reportId : reportIds) {
            //유효성 검사
            //1. reportPost 존재 여부
            Optional<ReportPost> reportPostObj = reportPostRepository.findById(reportId);
            if(!reportPostObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/ignore", "reportPostId에 해당하는 게시물이 없습니다.", ResponseEntity.class);
                continue;
            }
            //2. reportPost에 해당하는 post 존재 여부
            Optional<Post> postObj = postRepository.findById(reportPostObj.get().getPost().getId());
            if(!postObj.isPresent()) {
                ErrorService.send(HttpStatus.NOT_FOUND.value(), "/admin/report/ignore", "신고내역과 일치하는 게시물이 없습니다.", ResponseEntity.class);
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


}
