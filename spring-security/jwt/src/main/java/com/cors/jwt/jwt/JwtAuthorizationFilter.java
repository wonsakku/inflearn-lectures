package com.cors.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cors.jwt.auth.PrincipalDetails;
import com.cors.jwt.model.User;
import com.cors.jwt.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// BasicAuthenticaionFilter 가 있음.
// 권한이나 인증이 필요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 됨.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 됨.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        // super를 지워야 함.
//        super.doFilterInternal(request, response, chain);
        System.out.println("인증이나 권한이 필요한 주소가 요청됨.");

        final String jwtHeader = request.getHeader("Authorization");
        System.out.println("`````````````````````````jwtHeader`````````````````````````");
        System.out.println(jwtHeader);
        System.out.println("```````````````````````````````````````````````````````````");


        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("1");

        final String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");

        final String username = JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();

        // 서명이 정상적으로 됨
        if (username != null) {
            System.out.println("2");
            final User userEntity = userRepository.findByUsername(username);

            System.out.println("3");
            PrincipalDetails principalDetails = new PrincipalDetails(userEntity);

            //
            // Jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 만들어준다.
            // 서비스를 통해서 로그인을 진행하는 것이 아니라 임의로 Authentication 객체 생성하는 것이기 때문에 credential을 null로 처리해도 무방.
            System.out.println("4");
            Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

            System.out.println(authentication);
            System.out.println(authentication == null);

            // 강제로 시큐리티 세션에 접근하여 Authentication 객체를 저장.
            System.out.println("5");
            SecurityContextHolder.getContext().setAuthentication(authentication);

            System.out.println("6");
            chain.doFilter(request, response);
        }

        System.out.println("7");
    }
}




