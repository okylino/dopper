package com.carryking.dopper.websocket;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * @Author: carryking
 * @Date: 2018/4/21 22:45
 * @Description: 加载Properties辅助类
 */
public abstract class AbstractLoadProperties {

    @Bean
    protected PropertyPlaceholderConfigurer propertyPlaceholderConfigurer() {
        PropertyPlaceholderConfigurer propertyPlaceholderConfigurer = new PropertyPlaceholderConfigurer();
        String[] properties = classPathProperties();
        if (ArrayUtils.isEmpty(properties)) {
            return propertyPlaceholderConfigurer;
        }

        Resource[] resources = new Resource[properties.length];
        for(int n=0;n<properties.length;n++) {
            resources[n] = new ClassPathResource(properties[n]);
        }
        propertyPlaceholderConfigurer.setLocations(resources);
        return propertyPlaceholderConfigurer;
    }

    /**
     * 返回需要加载的ClassPath下的properties文件列表
     * @return
     */
    protected abstract String[] classPathProperties();
}
