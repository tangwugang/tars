package com.chehouzi.tars.core.configuration;


import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author twg
 */
@Configuration
@ConditionalOnExpression("!'${urls}'.isEmpty()")
@ConfigurationProperties(prefix = "urls")
public class IgnoreUrlsConfiguration {
    private List<String> anon = new ArrayList<>();

    public List<String> getAnon() {
        return anon;
    }

    public void setAnon(List<String> anon) {
        this.anon = anon;
    }

}
