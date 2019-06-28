package com.chehouzi.tars.oauth2.builder;

import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;

/**
 * @author twg
 * @since 2019/4/15
 */
@FunctionalInterface
public interface ClientDetailsConfigurerBuilder {

    /**
     * ClientDetailsService 自定义实现
     *
     * @param clients
     * @throws Exception
     */
    void build(ClientDetailsServiceConfigurer clients) throws Exception ;
}
