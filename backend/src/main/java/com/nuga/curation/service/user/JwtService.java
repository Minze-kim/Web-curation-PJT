package com.nuga.curation.service.user;

import com.nuga.curation.domain.user.entity.User;
import com.nuga.curation.exception.UnauthorizedException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service("jwtService")
public class JwtService {
    private static final String SALT = "ssafy304secret";
    public String create(User subject){
        String jwt = Jwts.builder()
                .setHeaderParam("typ","JWT")
                .setHeaderParam("regDate",System.currentTimeMillis())
                .setSubject(subject.getEmail())
                .claim("userId", subject.getUser_id())
                .claim("email", subject.getEmail())
                .claim("nickName",subject.getNickName())
                .setExpiration(new Date(System.currentTimeMillis()+(1000*60*60)))
                .signWith(SignatureAlgorithm.HS256, this.generateKey())
                .compact(); //직렬화
        return jwt;
    }
    private byte[] generateKey(){
        byte[] key = null;
        key = SALT.getBytes(UTF_8);
        return key;
    }
    public boolean isUsable(String jwt){
        System.out.println("isUsable");
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(this.generateKey())
                    .parseClaimsJws(jwt);
            return true;
        }catch (Exception e) {
            throw new UnauthorizedException();
        }
    }

    //jwt 데이터 가져오고 싶을 때,
    public Object get(String key) throws UnsupportedEncodingException {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");
        Jws<Claims> claims = null;
        try{
            claims = Jwts.parser()
                    .setSigningKey(SALT.getBytes("UTF-8"))
                    .parseClaimsJws(jwt);
        }catch (Exception e){
            throw new UnauthorizedException();
        }
        @SuppressWarnings("unchecked")
        Object value = claims.getBody().get(key);
        return value;
    }

    public boolean isHaveToken(){
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String jwt = request.getHeader("Authorization");
        if(jwt==null) return false;
        return true;
    }
}
