package com.campusbookstore.app.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    //간단한 뷰
    @GetMapping("/addPost")
    String viewAddPost () {
        return postService.viewAddPost();
    }
    @GetMapping("/detailPost")
    String viewDetailPost () {
        return postService.viewDetailPost();
    }
    @GetMapping("/editPost")
    String viewEdit () {
        return postService.viewEditPost();
    }
    @GetMapping("/search")
    String viewSearch () {
        return postService.viewSearch();
    }


    //책 등록
//    @PostMapping("/addPost")
//    String addPost (Post post) {
//        return postService.addPost(post);
//    }

    @PostMapping("/addPost")
    @ResponseBody
    ResponseEntity<Post> addPost (Post post) {
        return postService.addPost(post);
    }

    //찜한 리스트 추가 요청
    @PostMapping("/wish")
    void addWish (Long postId, Long memberId) {
        postService.addWish(postId, memberId);
    }

    //헤더의 검색바 작성
    @PostMapping("/search")
    String searching(String keyword, Model model) {
        return postService.searching(keyword, model);
    }

}
