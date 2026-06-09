package com.team7.aspect;

import com.team7.db.model.log.RequestLog;
import com.team7.db.repository.log.RequestLogRepository;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Request;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class AOPLogger {

    private final RequestLogRepository requestLogRepository;
    // 특정 패키지의 모든 메서드를 대상으로 설정
    @Pointcut("execution(* com.team7.controller..*(..)) && !execution(* com.team7.controller.HomeController.postLogout(..))")
    public void allRequestsExceptLogout() {}


    @Before("allRequestsExceptLogout()")
    public void logBefore(JoinPoint joinPoint) {
        // 메서드 호출 전에 로그 출력

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ipAddress = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String methodType = request.getMethod();



    }

    @AfterReturning(pointcut = "allRequestsExceptLogout()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        // 메서드 실행 후 로그 출력

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String ipAddress = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String methodType = request.getMethod();
        int status = response.getStatus();

        RequestLog requestLog = new RequestLog(0L, ipAddress, username, url, methodType, Integer.toString(status));


        requestLogRepository.save(requestLog);
    }

    @Around("execution(* com.team7.controller.HomeController.postLogout(..))")
    public void logoutLogAfterReturning(ProceedingJoinPoint joinPoint){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String ipAddress = request.getRemoteAddr();
        String url = request.getRequestURL().toString();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String methodType = request.getMethod();

        log.info(username + " trying to logout");

        try{

            joinPoint.proceed();


        } catch (Throwable e) {
            log.error("some error e");
            throw new RuntimeException(e);
        }
        finally{
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
            int status = response.getStatus();
            requestLogRepository.save(new RequestLog(0L, ipAddress, username, url, methodType, Integer.toString(status)));

        }

    }

}
