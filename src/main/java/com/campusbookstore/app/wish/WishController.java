package com.campusbookstore.app.wish;

import com.campusbookstore.app.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class WishController {
    private final PostService postService;
    private final WishService wishService;

    @GetMapping("/wish")
    String viewWish(Model model, Authentication auth) {
        return wishService.viewWish(model, auth);
    }

    //장바구니 추가 요청
    @PostMapping("/wishPlus")
    ResponseEntity<String> addWish(Long postId, Long quantity, Authentication auth) {
        return wishService.addWish(postId, quantity, auth);
    }

    @DeleteMapping("/wishDelete")
    String deleteWish(Long wishId, Authentication auth) {
        return wishService.deleteWish(wishId, auth);
    }
}
