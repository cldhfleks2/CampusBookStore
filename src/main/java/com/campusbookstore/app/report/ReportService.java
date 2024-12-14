package com.campusbookstore.app.report;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final ReportPostRepository reportPostRepository;
    private final ReportReviewRepository reportReviewRepository;

    String viewLogin(){
        return "admin/adminLogin";
    }

    String viewReport(Model model, Authentication auth){
        List<ReportPost> reportPosts = reportPostRepository.findAll();

        model.addAttribute("reportPosts", reportPosts);
        return "admin/report";
    }
}
