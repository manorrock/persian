package com.manorrock.persian;

import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.CredentialValidationResult;
import jakarta.security.enterprise.identitystore.IdentityStore;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.security.Principal;
import java.util.Base64;

/**
 * The Security Filter.
 * 
 * This filter checks for anonymous access and validates credentials using the IdentityStore.
 * If anonymous access is disabled, it sends a 403 Forbidden response.
 * If valid credentials are provided, it wraps the request to provide the authenticated user's details.
 * 
 */
public class SecurityFilter implements Filter {

    @Inject
    private IdentityStore identityStore;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String anonymousDisabled = httpRequest.getServletContext().getInitParameter("anonymousDisabled");
        if ("true".equalsIgnoreCase(anonymousDisabled)) {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Anonymous access is disabled");
            return;
        }

        String authHeader = httpRequest.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Basic ")) {
            String base64Credentials = authHeader.substring("Basic ".length()).trim();
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            String username = values[0];
            String password = values[1];

            UsernamePasswordCredential credential = new UsernamePasswordCredential(username, password);
            CredentialValidationResult result = identityStore.validate(credential);

            if (result.getStatus() == CredentialValidationResult.Status.VALID) {
                httpRequest = new HttpServletRequestWrapper(httpRequest) {
                    @Override
                    public Principal getUserPrincipal() {
                        return result.getCallerPrincipal();
                    }

                    @Override
                    public boolean isUserInRole(String role) {
                        return result.getCallerGroups().contains(role);
                    }

                    @Override
                    public String getRemoteUser() {
                        return result.getCallerPrincipal().getName();
                    }
                };
            }
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {
    }
}
