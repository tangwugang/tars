package com.chehouzi.tars.oauth2.configuration;

import com.chehouzi.tars.oauth2.builder.ClientDetailsConfigurerBuilder;
import com.chehouzi.tars.oauth2.error.CustomOAuth2ExceptionRenderer;
import com.chehouzi.tars.oauth2.error.CustomWebResponseExceptionTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.error.OAuth2ExceptionRenderer;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

/**
 * @author twg
 * @since 2019/4/3
 * 鉴权服务配置
 */
@Configuration
@EnableOAuth2Client
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClientDetailsConfigurerBuilder clientDetailsConfigurerBuilder;

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clientDetailsConfigurerBuilder.build(clients);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.exceptionTranslator(exceptionTranslator())
                //password
                .authenticationManager(authenticationManager)
                .userDetailsService(userDetailsService);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients()//支持浏览器直接输入，非Authorization
                .authenticationEntryPoint(authenticationEntryPoint())
                .passwordEncoder(passwordEncoder)
                .accessDeniedHandler(accessDeniedHandler());
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        OAuth2AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
        authenticationEntryPoint.setExceptionRenderer(exceptionRenderer());
        return authenticationEntryPoint;
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        OAuth2AccessDeniedHandler accessDeniedHandler = new OAuth2AccessDeniedHandler();
        accessDeniedHandler.setExceptionRenderer(exceptionRenderer());
        accessDeniedHandler.setExceptionTranslator(exceptionTranslator());
        return accessDeniedHandler;
    }

    @Bean
    public ClientDetailsConfigurerBuilder detailsConfigurerBuilder() {
        return clients -> {

        };
    }

    private OAuth2ExceptionRenderer exceptionRenderer() {
        return new CustomOAuth2ExceptionRenderer();
    }

    private WebResponseExceptionTranslator exceptionTranslator() {
        return new CustomWebResponseExceptionTranslator();
    }

}
