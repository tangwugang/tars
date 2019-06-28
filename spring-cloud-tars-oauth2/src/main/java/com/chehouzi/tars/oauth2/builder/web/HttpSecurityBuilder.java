package com.chehouzi.tars.oauth2.builder.web;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @author twg
 * @since 2019/4/15
 */
public interface HttpSecurityBuilder {

    /**
     * Override this method to configure the {@link HttpSecurity}. Typically subclasses
     * should not invoke this method by calling super as it may override their
     * configuration. The default configuration is:
     * <p>
     * <pre>
     * http.antMatcher("/**")
     * .authorizeRequests()
     * .antMatchers("/", "/login**", "/webjars/**")
     * .permitAll()
     * .anyRequest()
     * .authenticated().and().exceptionHandling()
     * .accessDeniedHandler(accessDeniedHandler)
     * .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
     * .and().logout().deleteCookies("HAOQIUSERTOKEN")
     * //自定义logout处理，返回json
     * .logoutSuccessHandler(customLogoutSuccessHandler())
     * .permitAll()
     * .and()
     * .csrf().disable()
     * //对于需要返回json格式的头信息的异常处理配置
     * .addFilterAfter(customErrorFilter(), ExceptionTranslationFilter.class)
     * .addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class)
     * .addFilterBefore(customCookieFilter(), BasicAuthenticationFilter.class)
     * .addFilterBefore(userLoginFilter(), BasicAuthenticationFilter.class);
     * </pre>
     *
     * @param http the {@link HttpSecurity} to modify
     * @return
     * @throws Exception if an error occurs
     */

    default HttpSecurity configure(HttpSecurity http) throws Exception {
        // @formatter:off
        return http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers("/", "/login**", "/webjars/**")
                .permitAll()
                .anyRequest()
                .authenticated().and().exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and().logout()
                .logoutSuccessUrl("/")
                .permitAll()
                .and()
                .csrf().disable();
        // @formatter:on
    }

    /**
     * builder authentication user info
     *
     * @param builder
     * @throws Exception
     */
    default void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.inMemoryAuthentication()
                .withUser("admin")
                .password("$2a$10$VbZFr4CSgr7/BmLMroXNNuI0EmGa9oIjM9ARdODUy5FZ0nBIRULNe")
                .roles("USER_ROLE");
    }
}
