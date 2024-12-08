package com.campusbookstore.app;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class BasicController {
    private final BasicService basicService;

    //간단한 뷰
    @GetMapping("/")
    String viewMain () {
        return basicService.viewMain();
    }
    @GetMapping("/main")
    String viewMain2 () {
        return basicService.viewMain();
    }





}
