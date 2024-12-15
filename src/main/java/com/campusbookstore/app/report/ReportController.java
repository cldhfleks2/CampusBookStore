package com.campusbookstore.app.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReportController {
    private final ReportService reportService;
    @GetMapping("/admin/login")
    String viewLogin() {
        return reportService.viewLogin();
    }

    @GetMapping("/admin/report")
    String viewReportPost(Model model) {
        return reportService.viewReport(model);
    }
    //reportId들을 받아서 reportPost 삭제
    @DeleteMapping("/admin/report/post/ignore")
    ResponseEntity<String> ignorePost(@RequestBody List<Long> reportIds) {
        return reportService.ignorePost(reportIds);
    }
    //reportId들을 받아서 reportPost에 해당하는 post를 실제로 삭제
    @DeleteMapping("/admin/report/post/delete")
    ResponseEntity<String> deletePost(@RequestBody List<Long> reportIds) {
        return reportService.deletePost(reportIds);
    }

    //reportId들을 받아서 reportReview 삭제
    @DeleteMapping("/admin/report/review/ignore")
    ResponseEntity<String> ignoreReview(@RequestBody List<Long> reportIds) {
        return reportService.ignoreReview(reportIds);
    }
    //reportId들을 받아서 reportReview에 해당하는 review를 실제로 삭제
    @DeleteMapping("/admin/report/review/delete")
    ResponseEntity<String> deleteReview(@RequestBody List<Long> reportIds) {
        return reportService.deleteReview(reportIds);
    }

}
