package com.campusbookstore.app.likey;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
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
}
