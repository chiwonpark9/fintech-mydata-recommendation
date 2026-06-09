package com.team7.service.account;

import com.team7.security.utils.JWTUtil;
import com.team7.db.repository.token.BlacklistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final BlacklistRepository blacklistRepository;
    private final JWTUtil jwtUtil;
}
