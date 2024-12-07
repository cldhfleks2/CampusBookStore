package com.campusbookstore.app.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/admin/login")
    String viewLogin() {
        return adminService.viewLogin();
    }

    @GetMapping("/admin/reportPost")
    String viewReportPost() {
        return adminService.viewReportPost();
    }

    @GetMapping("/admin/reportReview")
    String viewReportReview() {
        return adminService.viewReportReview();
    }
}
