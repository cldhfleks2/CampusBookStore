package com.campusbookstore.app.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

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

}
