package com.chehouzi.tars.oauth2.core;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author twg
 * @since 2019/4/25
 */
public interface DefaultUserDetails extends UserDetails {
    /**
     * 用户ID
     *
     * @return
     */
    Long getId();

    /**
     * 用户公司ID
     *
     * @return
     */
    Long getCompId();

    /**
     * 用户真实姓名
     *
     * @return
     */
    String getRealName();

    /**
     * 用户级别
     *
     * @return
     */
    Integer getUserLevel();

    /**
     * 部门ID
     *
     * @return
     */
    Long getDeptId();

    /**
     * 公司名
     *
     * @return
     */
    String getCompName();

    /**
     * 头像
     *
     * @return
     */
    String getHeadImageUrl();

    /**
     * 用户类型
     *
     * @return
     */
    Integer getUserType();

    /**
     * 盐值
     *
     * @return
     */
    String getSalt();
}
