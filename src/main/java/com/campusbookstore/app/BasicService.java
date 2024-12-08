package com.campusbookstore.app;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class BasicService {
    //간단한 뷰
    String viewMain () {
        return "main/main";
    }





}
