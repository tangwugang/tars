package com.chehouzi.tars.oauth2.configuration;

import com.chehouzi.tars.core.configuration.IgnoreUrlsConfiguration;
import com.chehouzi.tars.oauth2.builder.web.DefaultHttpSecurityBuilder;
import com.chehouzi.tars.oauth2.builder.web.HttpSecurityBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;

/**
 * @author twg
 * 认证服务
 * @see org.springframework.security.core.context.SecurityContextHolder //自定义session管理
 * //Filter 链中的第一个
 * @see org.springframework.security.web.context.SecurityContextPersistenceFilter
 * @since 2019/4/3
 */
@Slf4j
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class AuthenticationServerConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private IgnoreUrlsConfiguration ignoreUrls;

    @Autowired
    private HttpSecurityBuilder httpSecurityBuilder;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        httpSecurityBuilder.configure(http);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        httpSecurityBuilder.configure(builder);
    }

    @Bean("authenticationManager")
    @Qualifier
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean("userDetailsService")
    @Override
    protected UserDetailsService userDetailsService() {
        return super.userDetailsService();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        ignoreUrls.getAnon().stream().forEach(url -> {
            web.ignoring().antMatchers(url);
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpSecurityBuilder defaultHttpSecurityBuilder(){
        return new DefaultHttpSecurityBuilder();
    }

   public static class ClientResources {

        @NestedConfigurationProperty
        private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

        @NestedConfigurationProperty
        private ResourceServerProperties resource = new ResourceServerProperties();

        public AuthorizationCodeResourceDetails getClient() {
            return client;
        }

        public ResourceServerProperties getResource() {
            return resource;
        }
    }
}
