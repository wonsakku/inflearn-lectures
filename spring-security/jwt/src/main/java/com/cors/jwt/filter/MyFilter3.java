package com.cors.jwt.filter;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyFilter3 implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

//        if (request.getMethod().equalsIgnoreCase("post")) {
//            System.out.println("POST 요청됨");
//            String headerAuth = request.getHeader("Authorization");
//            System.out.println(headerAuth);
//
//            if ("cos".equals(headerAuth)) {
//                filterChain.doFilter(request, response);
//            } else {
//                PrintWriter out = response.getWriter();
//                out.println("인증안됨");
//            }
//        }

        filterChain.doFilter(request, response);
    }
}
