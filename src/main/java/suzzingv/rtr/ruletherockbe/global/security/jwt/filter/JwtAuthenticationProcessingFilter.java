package suzzingv.rtr.ruletherockbe.global.security.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import suzzingv.rtr.ruletherockbe.domain.user.domain.entity.User;
import suzzingv.rtr.ruletherockbe.domain.user.infrastructure.UserRepository;
import suzzingv.rtr.ruletherockbe.global.redis.RedisService;
import suzzingv.rtr.ruletherockbe.global.response.properties.ErrorCode;
import suzzingv.rtr.ruletherockbe.global.security.exception.AuthException;
import suzzingv.rtr.ruletherockbe.global.security.jwt.service.JwtService;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        checkLogout(request); //로그아웃한 사용자면 인증 처리 안함

        jwtService.extractAccessToken(request)
            .ifPresent(accessToken -> {
                if (!jwtService.isTokenValid(accessToken)) { //accessToken 만료 시
                    throw new AuthException(ErrorCode.SECURITY_INVALID_TOKEN);
                }
            });
        checkAccessTokenAndSaveAuthentication(request, response, filterChain);
    }

    private void checkLogout(HttpServletRequest request) {
        jwtService.extractAccessToken(request).ifPresent(accessToken -> {
            String value = redisService.getStrValue(accessToken);
            if (value.equals("logout")) {
                throw new AuthException(ErrorCode.SECURITY_UNAUTHORIZED);
            }
        });
    }

    private void checkAccessTokenAndSaveAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain) {
        jwtService.extractAccessToken(request)
            .flatMap(jwtService::extractSource)
            .flatMap(userRepository::findByPhoneNum).ifPresent(this::saveAuthentication);

        try {
            filterChain.doFilter(request, response);
        } catch (IOException | ServletException e) {
            throw new AuthException(ErrorCode.SERVER_ERROR);
        }
    }

    private void saveAuthentication(User myUser) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(myUser, null,
            authoritiesMapper.mapAuthorities(myUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
