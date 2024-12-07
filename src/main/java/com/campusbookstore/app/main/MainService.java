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










}
