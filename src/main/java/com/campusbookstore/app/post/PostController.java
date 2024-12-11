package com.campusbookstore.app.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping("/addPost")
    String viewAddPost () {
        return postService.viewAddPost();
    }
    @GetMapping("/detailPost/{postId}")
    String viewDetailPost (Model model, Authentication auth, @PathVariable Long postId,
                           RedirectAttributes redirectAttributes) {
        return postService.viewDetailPost(model, auth, postId, redirectAttributes);
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
    @PostMapping("/addPost")
    String addPost (PostDTO postDTO, Authentication auth) throws Exception {
        return postService.addPost(postDTO, auth);
    }



    //헤더의 검색바 작성
    @PostMapping("/search")
    String searching(String keyword, Model model) {
        return postService.searching(keyword, model);
    }

    //장바구니 추가 요청
    @PostMapping("/wishPlus")
    ResponseEntity<String> addWish(Long postId, Authentication auth) {
        return postService.addWish(postId, auth);
    }



}
