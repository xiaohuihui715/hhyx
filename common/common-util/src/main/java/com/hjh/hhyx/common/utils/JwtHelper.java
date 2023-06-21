package com.hjh.hhyx.common.utils;

import io.jsonwebtoken.*;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author 韩
 * @version 1.0
 */
public class JwtHelper {

    //过期时间
    private static long tokenExpiration = 24*60*60*1000;

    //加密密钥
    private static String tokenSignKey = "hhyx";

    /**
     * 根据useerid和userName生成token字符串
     * @param userId
     * @param userName
     * @return
     */
    public static String createToken(Long userId, String userName) {
        String token = Jwts.builder()
                .setSubject("hhyx-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)//私有部分
                .claim("userName", userName)
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)//加密
                .compressWith(CompressionCodecs.GZIP)//压缩成一行
                .compact();
        return token;
    }

    public static Long getUserId(String token) {
        if(StringUtils.isEmpty(token)) return null;

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
        // return 1L;
    }

    public static String getUserName(String token) {
        if(StringUtils.isEmpty(token)) return "";

        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (String)claims.get("userName");
    }

    public static void removeToken(String token) {
        //jwttoken无需删除，客户端扔掉即可。
    }

    public static void main(String[] args) {
        String token = JwtHelper.createToken(7L, "admin");
        System.out.println(token);
        System.out.println(JwtHelper.getUserId(token));
        System.out.println(JwtHelper.getUserName(token));
    }
}