package suzzingv.rtr.ruletherockbe.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import suzzingv.rtr.ruletherockbe.domain.user.infrastructure.UserRepository;
import suzzingv.rtr.ruletherockbe.global.redis.RedisService;
import suzzingv.rtr.ruletherockbe.global.security.jwt.entrypoint.CustomAuthenticationEntryPoint;
import suzzingv.rtr.ruletherockbe.global.security.jwt.filter.ExceptionHandlerFilter;
import suzzingv.rtr.ruletherockbe.global.security.jwt.filter.JwtAuthenticationProcessingFilter;
import suzzingv.rtr.ruletherockbe.global.security.jwt.handler.CustomAccessDeniedHandler;
import suzzingv.rtr.ruletherockbe.global.security.jwt.service.JwtService;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {

    private final JwtService jwtService;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .httpBasic(AbstractHttpConfigurer::disable)
            .csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .headers(headersConfigure -> headersConfigure
                .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
            .sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v0/user/login", "api/v0/user/verification-code").permitAll()
                .requestMatchers("/api/**").authenticated()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/**").permitAll())
            .exceptionHandling(customizer -> customizer
                .authenticationEntryPoint(customAuthenticationEntryPoint())
                .accessDeniedHandler(customAccessDeniedHandler()))
            .addFilterAfter(jwtAuthenticationProcessFilter(), LogoutFilter.class)
            .addFilterBefore(exceptionHandlerFilter(), JwtAuthenticationProcessingFilter.class);
        // 필터 순서: Logout filter -> jwtAuthenticationProcessFilter
        return http.build();
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, redisService, userRepository);
    }

    @Bean
    public ExceptionHandlerFilter exceptionHandlerFilter() {
        return new ExceptionHandlerFilter();
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint();
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}

