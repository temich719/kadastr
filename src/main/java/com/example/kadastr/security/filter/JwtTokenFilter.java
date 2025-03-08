package com.example.kadastr.security.filter;

import com.example.kadastr.security.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final JwtProvider jwtProvider;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtTokenFilter(JwtProvider jwtProvider, UserDetailsService userDetailsService) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Optional<String> optionalToken = jwtProvider.extractToken(request);
        if (optionalToken.isPresent()) {
            String token = optionalToken.get();
            System.out.println("filter token: " + token);
            String username = jwtProvider.getLoginFromToken(token);
            System.out.println("username froim token: " + username);

            if (nonNull(username) && isNull(SecurityContextHolder.getContext().getAuthentication())) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                System.out.println("here1");
                if (jwtProvider.isTokenValid(token, userDetails.getUsername())) {
                    System.out.println("here2");
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
    }
}
