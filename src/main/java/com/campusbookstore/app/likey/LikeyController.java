package com.campusbookstore.app.likey;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class LikeyController {
    private final LikeyRepository LikeyRepository;
    private final LikeyService LikeyService;

    @GetMapping("/likeyCount")
    @ResponseBody
    Long getLikeCount(Long postId) {
        return LikeyService.getLikeCount(postId);
    }

    //찜한 리스트 추가 요청
    @PostMapping("/likePlus")
    ResponseEntity<String> addLikey (Long postId, Authentication auth) {
        return LikeyService.addLikey(postId, auth);
    }
}
