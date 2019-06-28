package com.chehouzi.tars.oauth2;

import com.chehouzi.tars.core.configuration.IgnoreUrlsConfiguration;
import com.chehouzi.tars.oauth2.configuration.AuthenticationServerConfiguration;
import com.chehouzi.tars.oauth2.configuration.AuthorizationServerConfiguration;
import com.chehouzi.tars.oauth2.configuration.ResourceServerConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author twg
 * @since 2019/6/26
 */
@Configuration
@AutoConfigureBefore(WebMvcAutoConfiguration.class)
@Import({IgnoreUrlsConfiguration.class,
        ResourceServerConfiguration.class,
        AuthorizationServerConfiguration.class,
        AuthenticationServerConfiguration.class})
public class TarsOauth2AutoConfiguration {
}
