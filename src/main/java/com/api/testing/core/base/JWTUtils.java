package base;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// JSON Web Token Support
public class JWTUtils {

    public static String getJwt(long iat, long exp, String appId, String userId, String secret) {
        Claims claims = Jwts.claims();
        claims.put("iat", iat);
        claims.put("exp", exp);
        claims.put("appId", appId);
        claims.put("userId", userId);
        return Jwts.builder().setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret.getBytes()).compact();
    }
}

// Example. JWTUtils.getJwt(GENERATEIAT, GENERATEEXP,GENERATEAPPID, GENERATEUID,
// GENERATESECRET)