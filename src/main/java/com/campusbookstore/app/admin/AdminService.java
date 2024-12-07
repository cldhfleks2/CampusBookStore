package com.campusbookstore.app.admin;

import org.springframework.stereotype.Service;

@Service
public class AdminService {

    String viewLogin(){
        return "admin/adminLogin";
    }

    String viewReportPost(){
        return "admin/reportPost";
    }

    String viewReportReview(){
        return "admin/reportReview";
    }

}
