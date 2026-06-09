package com.team7.controller;

import com.team7.db.dto.CustomerRegisterDto;
import com.team7.db.model.entity.User;
import com.team7.db.repository.token.BlacklistRepository;
import com.team7.service.entitiy.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.http.HttpServletRequest;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class HomeControllerTest {
    @Mock
    private BlacklistRepository blacklistRepository;
    @Mock
    private CustomerService customerService;
    @Mock
    private HttpServletRequest request;
    @InjectMocks
    private HomeController homeController;

    private final String mockUsername = "user";
    private final ArrayList<GrantedAuthority> mockAuthority = new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));

    @Mock
    private Authentication authentication;

    @BeforeEach
    public void setup() {
        // Authentication 객체 설정
        lenient().when(authentication.getName()).thenReturn(mockUsername);
        Collection<GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));
        lenient().doReturn(authorities).when(authentication).getAuthorities();
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    public void getIndexPage_ShouldReturnUsername() {
        String result = homeController.getIndexPage(request, null);
        assertEquals(authentication.getName(), result);
    }



    @Test
    public void getRole_ShouldReturnUserRole() {
        ArrayList<GrantedAuthority> result = homeController.getRole(request, null);
        assertEquals(mockAuthority, result);
    }

    @Test
    public void postRegister_UserExists_ReturnConflict() {
        CustomerRegisterDto dto = new CustomerRegisterDto();
        dto.setAccountId("user1");
        dto.setEmail("user@example.com");
        dto.setPhone("1234567890");
        dto.setBirth("1995-12-13");


        when(customerService.findUserByAccountId(dto.getAccountId())).thenReturn(Optional.of(new User()));
        ResponseEntity<String> result = homeController.postRegister(dto);
        assertEquals(HttpStatus.CONFLICT, result.getStatusCode());
        assertEquals("아이디 중복.", result.getBody());
    }

    @Test
    public void postRegister_Success_ReturnsOk() {
        CustomerRegisterDto dto = new CustomerRegisterDto();
        dto.setAccountId("user2");
        dto.setEmail("user2@example.com");
        dto.setPhone("1234567890");
        dto.setBirth("1995-12-13");

        when(customerService.findUserByAccountId(anyString())).thenReturn(Optional.empty());
        when(customerService.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(customerService.findUserByPhone(anyString())).thenReturn(Optional.empty());

        ResponseEntity<String> result = homeController.postRegister(dto);
        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("회원가입 완료", result.getBody());
    }
}