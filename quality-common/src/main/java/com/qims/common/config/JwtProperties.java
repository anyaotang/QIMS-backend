package com.qims.common.config;

import com.qims.common.security.JwtTokenProvider;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;

/**
 * JWT 配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /** 签名密钥（至少 HS512 需 64 字节） */
    private String secret = "QIMS-Quality-Inspection-Management-System-JWT-Secret-Key-2024!!";

    /** Token 有效期（毫秒），默认 2 小时 */
    private long expiration = 2 * 60 * 60 * 1000L;

    /** Token 请求头名称 */
    private String header = "Authorization";

    /** Token 前缀 */
    private String prefix = "Bearer ";

    @Bean
    public SecretKey jwtSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }
}
