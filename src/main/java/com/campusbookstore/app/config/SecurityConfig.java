package com.campusbookstore.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //csrf 공격 방어기능을 꺼둠
        http.csrf((csrf) -> csrf.disable());

        http.authorizeHttpRequests((authorize) ->
                // 특정 페이지 로그인 검사 할지 결정 가능
                // 모든 URL의 로그인 체크해제
                authorize.requestMatchers("/**").permitAll()
        );

        // 2. 폼으로 로그인하겠다.
        http.formLogin((formLogin)
                        -> formLogin.loginPage("/login") // 로그인할 폼은 어딨냐?
                        .defaultSuccessUrl("/list")// 로그인 성공시 갈 url
                //              .failureUrl("/fail") // 로그인 실패시 갈 url
        );

        // 로그아웃은
        http.logout(logout -> logout.logoutUrl("/logout"));

        return http.build();
    }


}
