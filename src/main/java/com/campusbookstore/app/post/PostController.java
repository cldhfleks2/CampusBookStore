package com.campusbookstore.app.post;

import com.campusbookstore.app.report.ReportPost;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/addPost")
    String viewAddPost () {
        return postService.viewAddPost();
    }
    @GetMapping("/detailPost/{postId}")
    String viewDetailPost (Model model, Authentication auth, @PathVariable Long postId, RedirectAttributes redirectAttributes) {
        return postService.viewDetailPost(model, auth, postId, redirectAttributes);
    }
    @GetMapping("/editPost/{postId}")
    String viewEdit (@PathVariable Long postId, Authentication auth, Model model) {
        return postService.viewEditPost(postId, auth, model);
    }
    @GetMapping("/search")
    String viewSearch () {
        return postService.viewSearch();
    }

    //책 등록
    @PostMapping("/addPost")
    String addPost (PostDTO postDTO, Authentication auth, Model model) throws Exception {
        return postService.addPost(postDTO, auth);
    }
    //헤더의 검색바 작성시 호출
    //TODO 이것을 search view랑 합쳐보자.
    @PostMapping("/search")
    String searching(String keyword, Model model) {
        return postService.searching(keyword, model);
    }
    @PostMapping("/editPost")
    ResponseEntity<String> editPost (PostDTO postDTO, Authentication auth) throws Exception {
        return postService.editPost(postDTO, auth);
    }
    
    //TODO 게시물 삭제
    @DeleteMapping("/deletePost")
    ResponseEntity<String> deletePost (PostDTO postDTO, Authentication auth) {
        return postService.deletePost(postDTO, auth);
    }

    @PostMapping("/reportPost")
    ResponseEntity<String> reportPost(Long postId, Boolean inappropriateContent, Boolean spamOrAds, Boolean copyrightInfringement,
        Boolean misinformation, String otherReason, Authentication auth) {
        return postService.reportPost(postId,  inappropriateContent, spamOrAds, copyrightInfringement, misinformation, otherReason, auth);
    }

    @PostMapping("/reportReview")
    ResponseEntity<String> reportReview(Long reviewId, Boolean inappropriateContent, Boolean spamOrAds, Boolean copyrightInfringement,
                                      Boolean misinformation, String otherReason, Authentication auth) {
        return postService.reportReview(reviewId,  inappropriateContent, spamOrAds, copyrightInfringement, misinformation, otherReason, auth);
    }

}
