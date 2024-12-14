package com.campusbookstore.app;

import com.campusbookstore.app.member.AccountDetail;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BasicController {
    private final BasicService basicService;

    //간단한 뷰
    @GetMapping("/")
    String viewMain (Model model) {
        return basicService.viewMain(model);
    }
    @GetMapping("/main")
    String viewMain2 (Model model) {
        return basicService.viewMain(model);
    }
}
