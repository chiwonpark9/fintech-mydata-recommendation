    package com.team7.controller;


    import com.team7.db.dto.CustomerRegisterDto;
    import com.team7.db.model.entity.User;
    import com.team7.db.model.token.Blacklist;
    import com.team7.db.repository.token.BlacklistRepository;
    import com.team7.service.entitiy.CustomerService;
    import jakarta.servlet.http.HttpServletRequest;
    import jakarta.servlet.http.HttpServletResponse;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.Authentication;
    import org.springframework.security.core.GrantedAuthority;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.web.bind.annotation.*;

    import java.util.ArrayList;

    @Slf4j
    @RestController
    @RequiredArgsConstructor
    public class HomeController {
        private final BlacklistRepository blacklistRepository;
        private final CustomerService customerService;
        @GetMapping("/")
        @ResponseBody
        public String getIndexPage(HttpServletRequest request, HttpServletResponse response){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("returning " + authentication.getName());
            return authentication.getName();
        }


        @GetMapping("/role")
        public ArrayList<GrantedAuthority> getRole(HttpServletRequest request, HttpServletResponse response){
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("entered login");
            return new ArrayList<GrantedAuthority>(authentication.getAuthorities());
        }

        @PostMapping("/register")
        public ResponseEntity<String> postRegister(@RequestBody CustomerRegisterDto customerRegisterDto){

            System.out.println(customerRegisterDto.toString());

            if(customerService.findUserByAccountId(customerRegisterDto.getAccountId()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("아이디 중복.");
            }
            if(customerService.findUserByEmail(customerRegisterDto.getEmail()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 이메일.");
            }
            if(customerService.findUserByPhone(customerRegisterDto.getPhone()).isPresent()){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 휴대폰 번호.");
            }
            User newUser = new User(customerRegisterDto);
            customerService.save(newUser);
            return ResponseEntity.ok("회원가입 완료");
        }

        @PostMapping("/logout")
        public String postLogout(HttpServletRequest request, HttpServletResponse response){
            log.debug("logout controller");
            String token = request.getHeader("Authorization").split(" ")[1];
            Blacklist blacklist = new Blacklist(token);
            blacklistRepository.save(blacklist);
            SecurityContextHolder.clearContext();
            return SecurityContextHolder.getContext().getAuthentication().getName();


        }
    }
