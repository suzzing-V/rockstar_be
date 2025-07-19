package suzzingv.suzzingv.rockstar.global.security.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import suzzingv.suzzingv.rockstar.domain.user.exception.UserException;
import suzzingv.suzzingv.rockstar.domain.user.infrastructure.UserRepository;
import suzzingv.suzzingv.rockstar.global.redis.RedisService;
import suzzingv.suzzingv.rockstar.global.response.properties.ErrorCode;
import suzzingv.suzzingv.rockstar.global.security.exception.AuthException;

import java.time.Duration;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Getter
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String SOURCE_CLAIM = "source";
    private static final String LOGOUT = "logout";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final RedisService redisService;

    public String createAccessToken(String source) {
        Date now = new Date();
        return JWT.create()
            .withSubject(ACCESS_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))
            .withClaim(SOURCE_CLAIM, source)
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken() {
        Date now = new Date();
        return JWT.create()
            .withSubject(REFRESH_TOKEN_SUBJECT)
            .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))
            .sign(Algorithm.HMAC512(secretKey));
    }

    public String reissueRefreshToken(String refreshToken, String email) {
        deleteRefreshToken(refreshToken);
        String reissuedRefreshToken = createRefreshToken();
        updateRefreshToken(reissuedRefreshToken, email);
        return reissuedRefreshToken;
    }

    public String extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
            .filter(header -> header.startsWith(BEARER))
            .map(header -> header.replace(BEARER, ""))
                .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
            .filter(accessToken -> accessToken.startsWith(BEARER))
            .map(accessToken -> accessToken.replace(BEARER, ""))
                ;
    }

    public Optional<String> extractSource(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(accessToken) //검증
                .getClaim(SOURCE_CLAIM) //추출
                .asString());
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
    }

    //RefreshToken redis 저장
    public void updateRefreshToken(String refreshToken, String source) {
        redisService.setValue(refreshToken, source,
            Duration.ofMillis(refreshTokenExpirationPeriod));
    }

    public boolean isTokenValid(String token) {
        try {
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (JWTVerificationException e) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
        }
    }

    public void deleteRefreshToken(String refreshToken) {
        if (refreshToken == null) {
            throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
        }
        redisService.delete(refreshToken);
    }

    public void invalidAccessToken(String accessToken) {
        redisService.setValue(accessToken, LOGOUT,
            Duration.ofMillis(accessTokenExpirationPeriod));
    }

    public String checkRefreshToken(String refreshToken) {
        if (redisService.getStrValue(refreshToken).equals(RedisService.NOT_EXIST)) {
            throw new AuthException(ErrorCode.SECURITY_INVALID_REFRESH_TOKEN);
        }
        return redisService.getStrValue(refreshToken);
    }

    public void invalidTokens(String accessToken, String refreshToken) {
        isTokenValid(refreshToken);
        deleteRefreshToken(refreshToken);
        invalidAccessToken(accessToken);
    }
}