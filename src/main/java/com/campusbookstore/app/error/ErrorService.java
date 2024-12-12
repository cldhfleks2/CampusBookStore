package com.campusbookstore.app.error;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class ErrorService {
    /**
     * return ErrorService.send()로 사용한다. 뷰를 리턴해주므로
     * ResponsEntity에서는 사용 불가
     * @param status   HTTP 상태 코드 (HttpStatus.UNAUTHORIZED, HttpStatus.NOT_FOUND등)
     * @param path     에러가 발생한 요청 URL
     * @param message  오류 메시지
     * @param model    Spring Model 객체
     * @return         "error"페이지를 반환해줌
     */
    public static String send(int status, String path, String message, Model model) {
        model.addAttribute("status", status);
        model.addAttribute("path", path);
        model.addAttribute("message", message);
        return "error";
    }

    /**
     * return ErrorService.send()로 사용한다. 뷰를 리턴해주므로
     * ResponsEntity에서는 사용 불가
     * @param status   HTTP 상태 코드 (HttpStatus.UNAUTHORIZED, HttpStatus.NOT_FOUND등)
     * @param path     에러가 발생한 요청 URL
     * @param message  오류 메시지
     * @return         전달받은 status, path, meessage를 반환
     */
    public static ResponseEntity<String> send(int status, String path, String message) {
        return ResponseEntity.status(status).body(
                String.format("{\"status\":%d,\"path\":\"%s\",\"message\":\"%s\"}",
                status,
                path,
                message)
        );
    }
}
