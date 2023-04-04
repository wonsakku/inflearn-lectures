package com.cors.jwt.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cors.jwt.auth.PrincipalDetails;
import com.cors.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Date;

// 스프링 시큐리티에서 UsernamePasswordAuthenticationFilter 가 있음.
// /login 요청해서 username, password 전송 (post)
// UsernamePasswordAuthenticationFilter 가 동작함.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;


    
    // login 요청을 하면 로그인 시도를 위해서 실행되는 함수
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도 중");
        
        // 1. username, password 받아서
        try {

//            String input = null;
//
//            while( (input = br.readLine()) != null ){
//                System.out.println(input);
//            }
            ServletInputStream inputStream = request.getInputStream();
            final ObjectMapper objectMapper = new ObjectMapper();
            final User user = objectMapper.readValue(inputStream, User.class);

            System.out.println(user);

            final UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());


            // principalDetailsService 의 loadUserByUsername() 함수가 실행됨.
            // Authentication 객체가 생성되었다는건 id, password가 일치한다는 뜻. (로그인 처리가 됨?)
            final Authentication authentication = authenticationManager.authenticate(authenticationToken);


            // authentication 객체가 session 영역에 저장됨. => 로그인이 되었다는 뜻.
            final PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

            System.out.println(principalDetails.getUser().getUsername());

            // authentication 객체가 session 영역에 저장을 해야하고 그 방법이 return 해주면 됨
            // 리턴 이유는 권한 관리를 security가 대신 해주기 때문에 편하려고 하는거임.
            // 굳이 JWT 토큰을 사용하면서 세션을 만들 이유가 없음. 단지 권한 처리때문에 session에 넣어줍니다. 왜????



            return authentication;
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. 정상인지 로그인 시도를 해봄. authenticationManager로 로그인 시도를 하면
        // PrincipalDetailsService가 호출 loadUserByUsername() 함수 실행됨.
        
        
        // 3. PrincipalDetails를 세션에 담고 (권한 관리를 위해서)
        
        // 4. JWT 토큰을 만들어서 응답해주면 됨.
        return null;
    }

    // attemptAuthentication 실행 후 이니증이 정상적으로 되었으면 successfulAuthentication 항수가 실행됨.
    // JWT 토큰을 만들어서 request 요청한 사용자에게 JWT 토큰을 response 해주면 됨.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication 실행됨 : 인증이 완료되었다는 뜻");

        final PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        // claim 값에 내가 넣고 싶은 값을 넣으면 됨.
        final String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 1000*60*10))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512("cos"));

        response.addHeader("Authorization", "Bearer " + jwtToken);

//        super.successfulAuthentication(request, response, chain, authResult);
    }
}
