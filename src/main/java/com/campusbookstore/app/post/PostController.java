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
    //실제 검색 기능
    @GetMapping("/search")
    String viewSearch (String keyword, Integer pageIdx, Model model) {
        return postService.viewSearch(keyword, pageIdx, model);
    }

    //책 등록
    @PostMapping("/addPost")
    ResponseEntity<String> addPost (PostDTO postDTO, Authentication auth) throws Exception {
        return postService.addPost(postDTO, auth);
    }

    //게시물 수정
    @PostMapping("/editPost")
    ResponseEntity<String> editPost (PostDTO postDTO, Authentication auth) throws Exception {
        return postService.editPost(postDTO, auth);
    }
    //게시물 삭제
    @DeleteMapping("/deletePost")
    ResponseEntity<String> deletePost (PostDTO postDTO, Authentication auth) {
        return postService.deletePost(postDTO, auth);
    }
    //게시물 신고
    @PostMapping("/reportPost")
    ResponseEntity<String> reportPost(Long postId, Boolean inappropriateContent, Boolean spamOrAds, Boolean copyrightInfringement,
        Boolean misinformation, String otherReason, Authentication auth) {
        return postService.reportPost(postId,  inappropriateContent, spamOrAds, copyrightInfringement, misinformation, otherReason, auth);
    }
    //리뷰 신고
    @PostMapping("/reportReview")
    ResponseEntity<String> reportReview(Long reviewId, Boolean inappropriateContent, Boolean spamOrAds, Boolean copyrightInfringement,
                                      Boolean misinformation, String otherReason, Authentication auth) {
        return postService.reportReview(reviewId,  inappropriateContent, spamOrAds, copyrightInfringement, misinformation, otherReason, auth);
    }

}
