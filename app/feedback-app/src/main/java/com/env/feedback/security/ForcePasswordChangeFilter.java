package com.env.feedback.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ForcePasswordChangeFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        Authentication auth = org.springframework.security.core.context.SecurityContextHolder
                .getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated() && auth.getPrincipal() instanceof UserPrincipal principal) {

            String uri = request.getRequestURI();

            if (principal.getUser().isPasswordChangeRequired()
                    && !uri.equals("/user/change-password")
                    && !uri.startsWith("/css")
                    && !uri.startsWith("/js")
                    && !uri.startsWith("/images")) {

                response.sendRedirect("/user/change-password");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}