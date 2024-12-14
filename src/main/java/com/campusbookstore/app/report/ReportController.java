package com.campusbookstore.app.report;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    @GetMapping("/admin/login")
    String viewLogin() {
        return reportService.viewLogin();
    }

    @GetMapping("/admin/report")
    String viewReportPost(Model model, Authentication auth) {
        return reportService.viewReport(model, auth);
    }
}
