package com.campusbookstore.app.main;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {
    private final MainService mainService;

    @GetMapping("/")
    String viewMain () {
        return mainService.viewMain();
    }

    @GetMapping("/addPost")
    String viewAddPost () {
        return mainService.viewAddPost();
    }

    @GetMapping("/detailPost")
    String viewDetailPost () {
        return mainService.viewDetailPost();
    }

    @GetMapping("/login")
    String viewLogin () {

        return mainService.viewLogin();
    }

    @GetMapping("/register")
    String viewRegister () {

        return mainService.viewRegister();
    }

    @GetMapping("/mypage")
    String viewMyPage () {

        return mainService.viewMyPage();
    }






}
