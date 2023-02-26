package kr.legossol.ldap.api.organization.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.ldap")
public class LdapProperties {
    private String base;
}
