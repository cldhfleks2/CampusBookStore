package com.campusbookstore.app.main;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class MainService {


    String viewMain () {
        return "main/main";
    }

    String viewAddPost () {
        return "post/addPost";
    }

    String viewDetailPost () {
        return "post/detailPost";
    }

    String viewLogin () {
        return "member/login";
    }

    String viewRegister () {
        return "member/register";
    }

    String viewMyPage () {
        return "member/myPage";
    }

}
