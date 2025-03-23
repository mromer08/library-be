package com.ayd2.library.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties(prefix = "security.jwt")
@Getter
@Setter
public class JwtProperties {

    private final Access access = new Access();
    private final Refresh refresh = new Refresh();

    @Getter
    @Setter
    public static class Access {
        private String secretKey;
        private long expirationTime;
    }

    @Getter
    @Setter
    public static class Refresh {
        private String secretKey;
        private long expirationTime;

    }
}
