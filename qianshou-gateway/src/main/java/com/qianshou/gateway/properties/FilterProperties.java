package com.qianshou.gateway.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.ArrayList;

@ConfigurationProperties(prefix = "exclude")
public class FilterProperties {
    public ArrayList<String> getUri() {
        return uri;
    }

    public void setUri(ArrayList<String> uri) {
        this.uri = uri;
    }

    private ArrayList<String> uri;

}
