package org.carl.auth.utils;

import io.quarkus.security.UnauthorizedException;
import io.smallrye.jwt.auth.principal.JWTParser;
import io.smallrye.jwt.auth.principal.ParseException;
import io.smallrye.jwt.build.Jwt;

import java.util.HashSet;
import java.util.Set;

import org.carl.user.model.Role;
import org.carl.user.model.User;
import org.eclipse.microprofile.jwt.JsonWebToken;


public class JwtUtils {
    public static final String USER_ID = "userId";
    public static final String USERNAME = "username";
    public static final String EMAIl = "email";
    public static final String IMAGE_URL = "imageUrl";
    public static final String TYPE = "type";
    public static final String ISSUER = "https://org.carl/issuer";
    public static final String UPN = "notion@org.carl";

    public static JwtPojo generateToken(User user) {
        return new JwtPojo(generateAccentToken(user), generateRefreshToken(user.getId()));
    }


    public static String generateAccentToken(User user) {
        Set<String> set = new HashSet<>();
        for (Role role : user.getRoles()) {
            set.add(role.name());
        }
        return Jwt.issuer(ISSUER).upn(UPN).claim(USER_ID, user.getId())
                .claim(USERNAME, user.getUsername()).claim(EMAIl, user.getEmail())
                .claim(IMAGE_URL, user.getImageUrl()).expiresAt(System.currentTimeMillis() + 300000)
                .groups(set).sign();
    }

    public static String generateRefreshToken(Integer userId) {
        return Jwt.issuer(ISSUER).upn(UPN).claim(USER_ID, userId)
                .claim(TYPE, "refresh").expiresAt(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30)
                .sign();
    }

    public static boolean verifyToken(JWTParser jwtParser, String token) {
        try {
            JsonWebToken jsonWebToken = jwtParser.parseOnly(token);
            long expirationTime = jsonWebToken.getExpirationTime();
            return expirationTime > System.currentTimeMillis();
        } catch (ParseException e) {
            return false;
        }
    }

    public static JwtPojo convexAccentToken(JWTParser jwtParser, String accentToken, String refreshToken) {
        JsonWebToken webToken;
        try {
            jwtParser.parse(accentToken);
            return new JwtPojo(accentToken);
        } catch (ParseException parseException) {
            try {
                webToken = jwtParser.parseOnly(refreshToken);
                long expirationTime = webToken.getExpirationTime();
                if (expirationTime > System.currentTimeMillis()) {
                    User userInfo = getUserInfo(jwtParser.parseOnly(accentToken));
                    return new JwtPojo(JwtUtils.generateAccentToken(userInfo));
                }
                throw new UnauthorizedException("Authentication expired.");
            } catch (ParseException e) {
                throw new UnauthorizedException("Authentication expired.");
            }
        }
    }

    public static User getUserInfo(JsonWebToken jwt) {
        Integer id = Integer.parseInt(jwt.getClaim(USER_ID).toString());
        String username = jwt.getClaim(USERNAME);
        String email = jwt.getClaim(EMAIl);
        String imageUrl = jwt.getClaim(IMAGE_URL);
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setImageUrl(imageUrl);
        return user;
    }

    public static class JwtPojo {
        String accessToken;
        String refreshToken;

        public JwtPojo(String token) {

            this.accessToken = token;
        }

        public JwtPojo(String accessToken, String refreshToken) {
            this.accessToken = accessToken;
            this.refreshToken = refreshToken;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }
    }
}
