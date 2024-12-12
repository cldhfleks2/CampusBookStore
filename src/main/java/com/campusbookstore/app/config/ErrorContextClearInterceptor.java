package com.campusbookstore.app.config;

import com.campusbookstore.app.error.ErrorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ErrorContextClearInterceptor implements HandlerInterceptor {

    //모든 핸들러처리 이후에 확인
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
//        // ErrorService를 사용한경우 clearErrorContext메소드를 실행시킴
//        if (handler instanceof ErrorService) {
//            ErrorService.clearErrorContext();
//        }
    }
}
