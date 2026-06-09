package com.team7.security.filters;

import com.team7.db.model.entity.User;
import com.team7.db.repository.customer.CustomerRepository;
import com.team7.security.utils.JWTUtil;
import com.team7.db.repository.token.BlacklistRepository;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {


    private final JWTUtil jwtUtil;
    private final CustomerRepository customerRepository;
    private final BlacklistRepository blacklistRepository;

    private boolean isInBlacklist(String token){
        return blacklistRepository.findBlacklistByToken(token).isPresent();
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, ServletException, IOException {
        System.out.println("jwt filter");
        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
        String refresh = request.getHeader("Refresh");
        if(request.getParameter("username") != null || request.getParameter("password") != null){
            filterChain.doFilter(request, response);
            return;
        }
        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);
            //조건이 해당되면 메소드 종료 (필수)s
            return;
        }

        System.out.println("authorization now");
        //Bearer 부분 제거 후 순수 토큰만 획득
        String token = authorization.split(" ")[1];
        //토큰 소멸 시간 검증
        try{

            jwtUtil.isExpired(token);

        }
        catch(ExpiredJwtException e){
            System.out.println("Expired Token Accepted");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            filterChain.doFilter(request, response);

        }

        if (isInBlacklist(token)){
            System.out.println("Invalid Token - User dropped");
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            filterChain.doFilter(request, response);
            return;
        }


        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userEntity를 생성하여 값 set

        //UserDetails에 회원 정보 객체 담기
        User customUserDetails = customerRepository.findUserByAccountId(username).get();
        System.out.println("constructing authToken");
        //스프링 시큐리   티 인증 토큰 생성
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        //세션에 사용자 등록
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
