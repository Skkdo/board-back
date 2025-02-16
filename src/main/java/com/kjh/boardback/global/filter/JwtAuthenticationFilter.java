package com.kjh.boardback.global.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kjh.boardback.global.common.ResponseCode;
import com.kjh.boardback.global.common.ResponseDto;
import com.kjh.boardback.global.provider.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String accessToken = jwtProvider.getAccessToken(request);
        String refreshToken = jwtProvider.getRefreshToken(request);

        // Access Token, Refresh Token 둘 다 없는 요청
        if (accessToken == null && refreshToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Access Token 이 있는 요청
        if (accessToken != null) {
            String email = jwtProvider.validateAccessToken(accessToken);

            // Access Token 이 유효하지 않는 요청
            if (email == null) {
                ResponseDto responseDto = ResponseDto.fail(ResponseCode.ACCESS_TOKEN_EXPIRED);
                setResponse(response, responseDto);
                return;
            }

            // Access Token 이 유효한 요청
            setAuthentication(request, email);
            filterChain.doFilter(request, response);
            return;
        }

        // Refresh Token 이 있는 요청
        String email = jwtProvider.validateRefreshToken(refreshToken);

        // Refresh Token 이 유효하지 않는 요청
        if (email == null) {
            ResponseDto responseDto = ResponseDto.fail(ResponseCode.REFRESH_TOKEN_EXPIRED);
            setResponse(response, responseDto);
            return;
        // Refresh Token 이 유효한 요청이여도 Access Token 발급만 가능 하도록 인증 정보 X
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(HttpServletRequest request, String email) {
        AbstractAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(email, null, AuthorityUtils.NO_AUTHORITIES);
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authenticationToken);

        SecurityContextHolder.setContext(securityContext);
    }

    private void setResponse(HttpServletResponse response, ResponseDto responseDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(responseDto);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}