package com.drloveapp.drloveapp_backend.config;

import com.drloveapp.drloveapp_backend.service.UserDetailsServiceImpl;
import com.drloveapp.drloveapp_backend.utils.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class AuthTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            logger.info("Request path: {}", request.getRequestURI());
            logger.info("JWT extracted: {}", jwt != null ?
                    ("Token present (first 10 chars: " + jwt.substring(0, Math.min(10, jwt.length())) + "...)") :
                    "No token");
            if (jwt != null) {
                try {
                    boolean isValid = jwtUtils.validateJwtToken(jwt);
                    logger.info("JWT validation result: {}", isValid);

                    if (isValid) {
                        String username = jwtUtils.getUserNameFromJwtToken(jwt);
                        logger.info("Username from token: {}", username);

                        if (username != null && !username.isEmpty()) {
                            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                            if (userDetails != null) {
                                logger.info("User details loaded for: {}, authorities: {}",
                                        username, userDetails.getAuthorities());

                                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                                SecurityContextHolder.getContext().setAuthentication(authentication);
                                logger.info("Authentication set in SecurityContext");
                            } else {
                                logger.error("User details could not be loaded for username: {}", username);
                            }
                        } else {
                            logger.error("Username extracted from token is null or empty");
                        }
                    } else {
                        logger.warn("Token validation failed, not setting authentication");
                    }
                } catch (Exception e) {
                    logger.error("JWT validation error: {}", e.getMessage(), e);
                }
            } else {
                logger.debug("No JWT token found in request");
            }
        } catch (Exception e) {
            logger.error("Cannot set user authentication: {}", e.getMessage(), e);
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7);
        }

        return null;
    }
}