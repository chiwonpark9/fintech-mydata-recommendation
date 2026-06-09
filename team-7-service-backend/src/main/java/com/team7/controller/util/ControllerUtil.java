package com.team7.controller.util;

import com.team7.security.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ControllerUtil {
    private final JWTUtil jwtUtil;

    public String getTokenFromHeader(HttpServletRequest request){
        String token = request.getHeader("Authorization").split(" ")[1];
        return token;
    }
    public String getUserNameFromHeader(HttpServletRequest request){
        String customerAccountId = null;
        String token = request.getHeader("Authorization");
        if(token != null){
            token = token.split(" ")[1];
            customerAccountId = jwtUtil.getUsername(token);
        }
        return customerAccountId;
    }
}
