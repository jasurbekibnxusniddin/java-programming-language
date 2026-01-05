package uz.app.modules.auth.service;

import java.util.Set;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String issuer;

    public String generateToken(String username, Set<String> roles) {
        return Jwt.issuer(issuer)
                .subject(username)
                .upn(username)
                .groups(roles)
                .expiresIn(3600)
                .sign();
    }

    public String generateToken(String username) {
        return generateToken(username, Set.of("user"));
    }
}
