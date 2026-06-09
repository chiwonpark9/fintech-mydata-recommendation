package com.team7.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.team7.aspect.AOPLogger;
import com.team7.db.repository.log.RequestLogRepository;
import com.team7.security.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.w3c.dom.ls.LSOutput;

import java.util.Collection;
import java.util.Iterator;

@CrossOrigin()
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;

    private final RequestLogRepository requestLogRepository;


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("login filter");
        //클라이언트 요청에서 username, password 추출
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(request.toString());
        System.out.println("delivering payload ");
        System.out.println(username);
        System.out.println(password);
        //스프링 시큐리티에서 username과 password를 검증하기 위해서는 token에 담아야 함
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        //token에 담은 검증을 위한 AuthenticationManager로 전달
        return authenticationManager.authenticate(authToken);
    }

    //로그인 성공시 실행하는 메소드 (여기서 JWT를 발급하면 됨)
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {

        //UserDetailsS
        //Customer customUserDetails = (Customer) authentication.getPrincipal();

        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();

        String role = auth.getAuthority();
        System.out.println("role : " + role);
        String accessToken = jwtUtil.createJwt(username, role, 60*1000*60L);
        String refreshToken = jwtUtil.createJwt(username, role, 60*1000*60L*10);
        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh", "Bearer " + refreshToken);
//        response.setHeader("Access-Control-Expose-Headers", "Authorization");
//        response.addHeader("Access-Control-Expose-Headers", "Refresh");


        SecurityContextHolder.getContext().setAuthentication(authentication);


    }

    //로그인 실패시 실행하는 메소드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        System.out.println("AuthenticationFailed");
        response.setStatus(401);
    }
}