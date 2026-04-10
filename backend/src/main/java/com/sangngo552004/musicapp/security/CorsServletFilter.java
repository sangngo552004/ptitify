package com.sangngo552004.musicapp.security;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Servlet-level CORS filter.
 *
 * Runs at the Undertow/Servlet layer — BEFORE JAX-RS processing.
 * This guarantees that:
 *   1. OPTIONS preflight requests always receive CORS headers (HTTP 200).
 *   2. All other responses (GET, POST, PUT, DELETE) also receive CORS headers.
 *
 * The old JAX-RS ContainerRequestFilter/ContainerResponseFilter approach was
 * unreliable because WildFly can handle OPTIONS at the Undertow level before
 * the JAX-RS filter chain is even invoked.
 */
@WebFilter(urlPatterns = "/*", filterName = "CorsServletFilter")
public class CorsServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // No initialization needed
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String allowedOrigin = getAllowedOrigin();

        // Add CORS headers to every response
        response.setHeader("Access-Control-Allow-Origin", allowedOrigin);
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        response.setHeader("Access-Control-Max-Age", "1209600");
        response.setHeader("Vary", "Origin");

        // Short-circuit preflight (OPTIONS) immediately — no further processing needed
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Nothing to clean up
    }

    private String getAllowedOrigin() {
        String origin = System.getenv("ALLOWED_ORIGIN");
        if (origin == null || origin.isBlank()) {
            origin = System.getProperty("ALLOWED_ORIGIN");
        }
        return (origin == null || origin.isBlank()) ? "http://localhost:3000" : origin;
    }
}
