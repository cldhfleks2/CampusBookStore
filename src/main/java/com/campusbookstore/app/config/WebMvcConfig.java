package com.campusbookstore.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    //에러메시지 출력후 에러메시지 데이터 후처리를 위한 인터셉터 코드
    //NOTE: 생각해보니 요청이 끝나면 스레드가 자동으로 종료 될때 JVM이 알아서 데이터를 정리 할것이므로 필요없다.
//    @Autowired
//    private ErrorContextClearInterceptor errorContextClearInterceptor;
//
//    //인터셉터 등록
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(errorContextClearInterceptor);
//    }
}
