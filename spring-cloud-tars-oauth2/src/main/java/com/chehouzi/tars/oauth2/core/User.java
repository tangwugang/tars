package com.chehouzi.tars.oauth2.core;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * @author twg
 * @since 2019/1/4
 */
@EqualsAndHashCode
@NoArgsConstructor
public class User implements DefaultUserDetails {
    private Long id;
    private Long compId;
    private String username;
    @JsonIgnore
    private String password;
    private String realName;
    private Integer userLevel;
    private Integer userType;
    @JsonIgnore
    private String salt;
    private Long deptId;
    private String compName;
    @JsonIgnore
    private String headImageUrl;
    private Collection<? extends GrantedAuthority> authorities;
    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        this(username, password, authorities, null, null, "", null,null, "", null, "", "", true, true, true, true);

    }

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities,
                Long id, Long compId, String realName, Integer userLevel, Integer userType, String salt, Long deptId, String compName,
                String headImageUrl, boolean accountNonExpired, boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {
        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.salt = salt;
        this.id = id;
        this.compId = compId;
        this.realName = realName;
        this.userLevel = userLevel;
        this.userType = userType;
        this.deptId = deptId;
        this.compName = compName;
        this.headImageUrl = headImageUrl;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id,
                String realName, Integer userLevel, Integer userType, String salt, String headImageUrl, boolean accountNonExpired,
                boolean accountNonLocked, boolean credentialsNonExpired, boolean enabled) {

        this.username = username;
        this.password = password;
        this.authorities = authorities;
        this.salt = salt;
        this.id = id;
        this.realName = realName;
        this.userLevel = userLevel;
        this.userType = userType;
        this.headImageUrl = headImageUrl;
        this.accountNonExpired = accountNonExpired;
        this.accountNonLocked = accountNonLocked;
        this.credentialsNonExpired = credentialsNonExpired;
        this.enabled = enabled;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public Long getCompId() {
        return compId;
    }

    @Override
    public String getRealName() {
        return realName;
    }

    @Override
    public Integer getUserLevel() {
        return userLevel;
    }

    @Override
    public Integer getUserType() {
        return userType;
    }

    @Override
    public String getSalt() {
        return salt;
    }

    @Override
    public Long getDeptId() {
        return deptId;
    }

    @Override
    public String getCompName() {
        return compName;
    }

    @Override
    public String getHeadImageUrl() {
        return headImageUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
