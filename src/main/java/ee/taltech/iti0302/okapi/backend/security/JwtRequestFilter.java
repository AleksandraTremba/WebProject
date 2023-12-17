package ee.taltech.iti0302.okapi.backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@AllArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtTokenProvider tokenProvider;
    private Optional<String> getToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer"))
            return Optional.empty();

        return Optional.of(header.substring("Bearer ".length()));
    }

    private Claims parseToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(tokenProvider.key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private UsernamePasswordAuthenticationToken buildAuthToken(Claims body) {
        return new UsernamePasswordAuthenticationToken(body.get("username"), "",
                List.of(new SimpleGrantedAuthority("USER")));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        Optional<String> jwt = getToken(request);
        if (jwt.isEmpty()) {
            filterChain.doFilter(request, response);
            return ;
        }

        Claims tokenBody = parseToken(jwt.get());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(buildAuthToken(tokenBody));

        filterChain.doFilter(request, response);
    }
}
