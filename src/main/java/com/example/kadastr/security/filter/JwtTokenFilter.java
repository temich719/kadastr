package com.example.kadastr.security.filter;

import com.example.kadastr.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static java.util.Objects.nonNull;
import static java.util.Objects.isNull;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private static final String JWT_INVALID_MESSAGE = "Invalid or expired JWT token";

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            Optional<String> optionalToken = jwtProvider.extractToken(request);
            if (optionalToken.isPresent()) {
                String token = optionalToken.get();
                String username = jwtProvider.getLoginFromToken(token);

                if (nonNull(username) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                    if (jwtProvider.isTokenValid(token, userDetails.getUsername())) {
                        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities() //credentials = null 'cause we use jwt auth
                        );
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        //while current request is executing our user stores at context
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            handleAuthException(response, JWT_INVALID_MESSAGE, HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void handleAuthException(HttpServletResponse response, String message, int status) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write("{\"status\":\"ERROR\", \"message\":\"" + message + "\"}");
    }
}
