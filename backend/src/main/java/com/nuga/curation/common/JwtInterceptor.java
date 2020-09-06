package com.nuga.curation.common;

import com.nuga.curation.exception.UnauthorizedException;
import com.nuga.curation.service.user.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@Component
public class JwtInterceptor implements HandlerInterceptor {
    private static final String HEADER = "Authorization";
    private final JwtService jwtService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("Authorization");
        String x = request.getMethod();
        if(request.getMethod().equals("OPTIONS")) return true;
        if(token!=null && jwtService.isUsable(token)){
            return true;
        }else{
            throw new UnauthorizedException();
        }
    }
}
