package com.nuga.curation.common;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final String[] EXCLUDE_PATHS = {
            "/users/login/**",
            "/users/signup/**",
            "/users/email/confirm/**",
            "/users/signup/**",
            "/users/findpw/**",
            "/users/follower/**",
            "/users/following/**",
            "/users/profile/**",
            "/users/serach/**",
            "/swagger-ui.html", "/swagger-resources/**", "/webjars/**", "/v2/**",
            "/articles/detail/**", "/articles/sell/**","/articles/all","/articles/search/**","/articles/category/**",
            "/users/search/**"
    };
    @Autowired
    private JwtInterceptor jwtInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(EXCLUDE_PATHS);

    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("*")
                .allowedOrigins("*")
                .exposedHeaders("Authorization");
    }

}
