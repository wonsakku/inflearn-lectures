package com.cors.jwt.config;

import com.cors.jwt.filter.MyFilter1;
import com.cors.jwt.filter.MyFilter3;
import com.cors.jwt.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity // Security 설정 활성화
@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        /**
         *  http.addFilter(new Filter1()); 을 했을 경우
         *  org.springframework.beans.factory.BeanCreationException:
         *  Error creating bean with name 'springSecurityFilterChain' defined in class path resource [org/springframework/security/config/annotation/web/configuration/WebSecurityConfiguration.class]:
         *  Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException:
         *  Failed to instantiate [javax.servlet.Filter]:
         *  Factory method 'springSecurityFilterChain' threw exception;
         *  nested exception is java.lang.IllegalArgumentException:
         *  The Filter class com.cors.jwt.filter.MyFilter1 does not have a registered order and cannot be added without a specified order.
         *  Consider using addFilterBefore or addFilterAfter instead.
         */
//        http.addFilter(new MyFilter1());
//        http.addFilterBefore(new MyFilter3(), BasicAuthenticationFilter.class);
        // 시큐리티 필터가 필터 설정에서 설정한 필터보다 먼저실행됨.
        // 커스터마이징한 필터를 가장 먼저 실행시키려면 SecurityContextPersistenceFilter 앞에 필터 설정.
        http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class);

        http.csrf()
                .disable();
        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(corsFilter) // @CrossOrigin 은 인증이 없을 때 사용 가능, 인증이 필요한 경우 시큐리티 필터 등록해야함.
                .formLogin().disable()
                .httpBasic().disable() // http header에 인증정보(ID, PWD)를 넣어서 요청을 보내는 방식(httpBasic), Bearer 방식은 토큰을 넣어서 요청을 보내는 방식
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER') or hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll();


    }
}
