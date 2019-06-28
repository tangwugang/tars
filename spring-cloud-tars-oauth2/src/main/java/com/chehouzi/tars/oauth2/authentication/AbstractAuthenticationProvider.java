package com.chehouzi.tars.oauth2.authentication;

import com.chehouzi.tars.oauth2.core.DefaultUserDetails;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.cache.NullUserCache;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;

/**
 * @author twg
 * @since 2019/4/21
 */
public abstract class AbstractAuthenticationProvider implements AuthenticationProvider {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private UserCache userCache = new NullUserCache();

    protected AbstractAuthenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())) {
            return null;
        }
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();
        boolean cacheWasUsed = true;
        UserDetails userDetail = this.userCache.getUserFromCache(username);
        if (Objects.isNull(userDetail)) {
            cacheWasUsed = false;
            try {
                userDetail = userDetailsService.loadUserByUsername(username);
                if (userDetail == null) {
                    throw new InternalAuthenticationServiceException(
                            "UserDetailsService returned null, which is an interface contract violation");
                }
                DefaultUserDetails details = (DefaultUserDetails) userDetail;
                if (!passwordEncoder.matches(password + details.getSalt(), userDetail.getPassword())) {
                    errorHandler();
                }
            } catch (UsernameNotFoundException ex) {
                throw ex;
            } catch (InternalAuthenticationServiceException ex) {
                throw ex;
            } catch (Exception ex) {
                throw new InternalAuthenticationServiceException(ex.getMessage(), ex);
            }
        }
        if (!cacheWasUsed) {
            userCache.putUserInCache(userDetail);
        }
        return buildAuthenticationToken(authentication, userDetail);
    }

    /**
     * 构建Authentication
     *
     * @param authentication
     * @param userDetails
     * @return
     */
    protected abstract Authentication buildAuthenticationToken(Authentication authentication, UserDetails userDetails);

    /**
     * 异常处理
     *
     * @throws AuthenticationException
     */
    protected void errorHandler() throws AuthenticationException {

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return false;
    }
}
