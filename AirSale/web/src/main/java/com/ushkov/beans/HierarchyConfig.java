package com.ushkov.beans;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("roleconfiguration")
@Data
public class HierarchyConfig {
    private String hierarchy;
}
