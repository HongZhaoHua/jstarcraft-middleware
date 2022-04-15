package com.jstarcraft.cloud.eureka.configurer;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.jstarcraft.cloud.log.LogFilter;

@Configuration
public class LogFilterConfigurer {

    @Bean
    public LogFilter getLogFilter() {
        LogFilter filter = new LogFilter();
        return filter;
    }

    @Bean
    public FilterRegistrationBean<LogFilter> registrationBean(LogFilter filter) {
        FilterRegistrationBean<LogFilter> bean = new FilterRegistrationBean<>(filter);
        bean.addUrlPatterns("/*");
        bean.setOrder(FilterRegistrationBean.HIGHEST_PRECEDENCE);
        return bean;
    }

}
