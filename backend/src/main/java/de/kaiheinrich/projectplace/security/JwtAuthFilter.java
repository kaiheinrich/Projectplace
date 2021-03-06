package de.kaiheinrich.projectplace.security;

import de.kaiheinrich.projectplace.security.service.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Service
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorization = httpServletRequest.getHeader("Authorization");
        if(authorization == null || authorization.isBlank()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        String token = authorization.replace("Bearer ", "").trim();
        try {
            Claims claims = jwtUtils.parseToken(token);
            if(!jwtUtils.isExpired(claims)) {
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(
                        claims.getSubject(),
                        "",
                        Collections.emptyList()
                ));
            }

        } catch (Exception exception) {
            System.out.println(exception);
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
